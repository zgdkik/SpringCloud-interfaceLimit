package com.yasaka.stock.filter;

import com.google.common.util.concurrent.RateLimiter;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.ReflectionUtils;

import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName RateLimitZuulFilter
 * @Description 功能说明：单节点Zuul下的限流,该类基于令牌桶算法来完成限流
 * @Author 刘松
 * @Date 2018/5/8 13:40
 * @Version 1.0
 **/

public class RateLimitZuulFilter extends ZuulFilter {

    private int size = 2;

    private final RateLimiter rateLimiter = RateLimiter.create(size);


    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        // 这边的order一定要大于org.springframework.cloud.netflix.zuul.filters.pre.PreDecorationFilter的order
        // 也就是要大于5
        // 否则，RequestContext.getCurrentContext()里拿不到serviceId等数据。
        return FilterConstants.PRE_DECORATION_FILTER_ORDER +1;
    }

    /**
    * @Description 方法说明：Spring Boot Actuator提供的Metrics 能力进行实现基于内存压力的限流
     *  ——当可用内存低于某个阈值就开启限流，否则不开启限流。
    * @Author 刘松
    * @Date 2018/5/8 14:48
    * @param
    * @return boolean
    **/
    @Override
    public boolean shouldFilter() {
/*    // 这里可以考虑弄个限流开启的开关，开启限流返回true，关闭限流返回false，你懂的。
            Collection<Metric<?>> metrics = systemPublicMetrics.metrics();
            Optional<Metric<?>> freeMemoryMetric = metrics.stream()
                    .filter(t -> "mem.free".equals(t.getName()))
                    .findFirst();
    // 如果不存在这个指标，稳妥起见，返回true，开启限流
            if (!freeMemoryMetric.isPresent()) {
                return true;
            }
            long freeMemory = freeMemoryMetric.get()
                    .getValue()
                    .longValue();
    // 如果可用内存小于1000000KB，开启流控
            return freeMemory < 1500000L;*/
        return true;
    }

    @Override
    public Object run() {
        try {
            System.out.println("RateLimitZuulFilter过滤器开始执行了!");
            RequestContext currentContext = RequestContext.getCurrentContext();

            HttpServletResponse response = currentContext.getResponse();

            if (!rateLimiter.tryAcquire()) {

                HttpStatus httpStatus = HttpStatus.TOO_MANY_REQUESTS;

                response.setContentType(MediaType.TEXT_PLAIN_VALUE);

                response.setStatus(httpStatus.value());

                response.getWriter().append(httpStatus.getReasonPhrase());
                currentContext.setSendZuulResponse(false);

                throw new ZuulException(httpStatus.getReasonPhrase(), httpStatus.value(), httpStatus.getReasonPhrase());
            }
        } catch (Exception e) {
            ReflectionUtils.rethrowRuntimeException(e);
        }
        return null;
    }

}
