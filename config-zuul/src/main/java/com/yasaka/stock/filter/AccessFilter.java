package com.yasaka.stock.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.http.ServletInputStreamWrapper;
import io.jmnarloch.spring.cloud.ribbon.support.RibbonFilterContextHolder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;

/** 
* @Description 方法说明：灰度版本控制
* @Author 刘松
* @Date 2018/5/8 11:07 
* @param 
* @return   
**/  

@Component
@RefreshScope
public class AccessFilter extends ZuulFilter {

    @Value("${version}")
    String version;


    @Override
    public String filterType() {
        return PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return FilterConstants.PRE_DECORATION_FILTER_ORDER + 2;
    }

    @Override
    public boolean shouldFilter() {
        // a filter has already forwarded
        // a filter has already determined serviceId
/*        return !ctx.containsKey(FORWARD_TO_KEY)
                && !ctx.containsKey(SERVICE_ID_KEY);*/
        return true;
    }

    @Override
    public Object run() {
        System.out.println("### filter in pre.");
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();

        fixParams(ctx);

        String tempVersion = request.getParameter("version");
        if (tempVersion == null || "".equals(tempVersion)) {
            return null;
        }

        //灰度示例
        RibbonFilterContextHolder.clearCurrentContext();
        System.out.println("version="+version);

        if("A".equals(version)){
            if (tempVersion.equals("gray")) {
                RibbonFilterContextHolder.getCurrentContext().add("lancher", "A");
            } else {
                RibbonFilterContextHolder.getCurrentContext().add("lancher", "B");
            }
        }
        else{
            if (tempVersion.equals("gray")) {
                RibbonFilterContextHolder.getCurrentContext().add("lancher", "B");
            } else {
                RibbonFilterContextHolder.getCurrentContext().add("lancher", "A");
            }
        }



/*        if (tempVersion.equals("gray")) {

        } else {
            RibbonFilterContextHolder.getCurrentContext().add("lancher", "B");
        }*/

        return null;
    }


    private void fixParams(RequestContext ctx) {
        try {
            InputStream in = (InputStream) ctx.get("requestEntity");
            if (in == null) {
                in = ctx.getRequest().getInputStream();
            }

            String body = StreamUtils.copyToString(in, Charset.forName("UTF-8"));
            System.out.println("### 修改前 body:" + body);
            body = "name=abcde";
            System.out.println("### 修改后 body:" + body);

            final byte[] bytes = body.getBytes("UTF-8");
            ctx.setRequest(new HttpServletRequestWrapper(ctx.getRequest()) {
                @Override
                public ServletInputStream getInputStream() throws IOException {
                    return new ServletInputStreamWrapper(bytes);
                }

                @Override
                public int getContentLength() {
                    return bytes.length;
                }

                @Override
                public long getContentLengthLong() {
                    return bytes.length;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
