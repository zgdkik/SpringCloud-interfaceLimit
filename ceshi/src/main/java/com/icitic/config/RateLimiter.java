package com.icitic.config;

import com.google.common.collect.Lists;
import com.google.common.io.Files;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.io.File;
import java.nio.charset.Charset;

/**
 * @ClassName RateLimiter
 * @Description 功能说明：
 * @Author 刘松
 * @Date 2018/5/21 17:20
 * @Version 1.0
 **/
@Component
public class RateLimiter {

    @Value("${redis.id:10.14.38.201}")
    private String redisId;

    @Value("${redis.port:6380}")
    private int port;

    //设置redis缓存的前缀名
    private String limitStr = "limit:";

    private String limitTime = "limitTime:";

    private String limitCount = "limitCount:";


    /**
    * @Description 方法说明：
    * @Author 刘松
    * @Date 2018/5/22 13:40
    * @param url, value, expireTim [ url表示存储的URL地址,value表示限流次数,expireTime表示过期时间(秒)]
    * @return java.lang.String
    **/
    public String setStore(String url,String limitCount,long expireTime){
        Jedis jedis = new Jedis(redisId, port);
        jedis.auth("abab1456");

        //设置限流的URL地址,key为地址,value为限流次数
        String url_key = limitStr + url;
        jedis.set(url_key,"0");

        String limitCount_key = this.limitCount + url;
        jedis.set(limitCount_key,limitCount);

        String expire_key = limitTime + url;

        long expire_date = System.currentTimeMillis()/ 1000 + expireTime;
        jedis.set(expire_key,String.valueOf(expire_date));

        return "设置存储成功";
    }

    public String deleteStore(String url){
        Jedis jedis = new Jedis(redisId, port);
        jedis.auth("abab1456");

        String url_key = limitStr + url;
        jedis.del(url_key);

        String expire_key = limitTime + url;
        jedis.del(expire_key);

        String limitCount_key = this.limitCount + url;
        jedis.del(limitCount_key);

        return "删除存储成功";
    }

    public boolean acquire(String url) throws Exception {
        String filePath = RateLimiter.class.getResource("/").getPath() + "limit.lua";
        String luaScript = Files.toString(new File(filePath), Charset.defaultCharset());
        Jedis jedis = new Jedis(redisId, port);
        jedis.auth("abab1456");

        String expire_key = limitTime + url;

        long currentTime = System.currentTimeMillis()/ 1000 ;

        String temp_expireTime = jedis.get(expire_key);

        if(temp_expireTime == null || temp_expireTime.equals("")){
            return  true;
        }

        long expireTime = Long.parseLong(temp_expireTime);

        if(currentTime < expireTime){
            String url_key = limitStr + url;

            String limitCount_key = this.limitCount + url;
            String tempLimitCount = jedis.get(limitCount_key);

            long result = (Long)jedis.eval(luaScript, Lists.newArrayList(url_key), Lists.newArrayList(tempLimitCount));
            return result == 1;
        }
        else{
            return true;
        }
    }

}
