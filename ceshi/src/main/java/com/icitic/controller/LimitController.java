package com.icitic.controller;

import com.icitic.config.RateLimiter;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @ClassName LimitController
 * @Description 功能说明：
 * @Author 刘松
 * @Date 2018/5/21 17:08
 * @Version 1.0
 **/
@RestController
public class LimitController {
    @Autowired
    private RestTemplate restTemplate;

    private String url = "http://www.baidu.com/";

    @Autowired
    private RateLimiter rateLimiter;

    @RequestMapping("/setLimiter3")
    public void setRateLimiterParam(@RequestParam(value = "requestCount") int requestCount,
                                    @RequestParam(value = "url") String url){

        if(requestCount > 0){
            rateLimiter.setStore(url,String.valueOf(requestCount),1000);
        }
        else{
            rateLimiter.deleteStore(url);
        }
        this.url = url;
        System.out.println("setLimiter方法执行了! requestCount="+requestCount+" url="+url);
    }



    @RequestMapping("/method3")
    @HystrixCommand(fallbackMethod = "findByIdFallback",
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE")
            })
    public String sayHello() throws Exception{
        if (!rateLimiter.acquire(url)) {
            throw new Exception("限流了!!!!");
        }
        return restTemplate.getForObject(url,String.class);
    }

    public String findByIdFallback() {
        System.out.println("======================== findByIdFallback " + Thread.currentThread().getThreadGroup() + " - " + Thread.currentThread().getId() + " - " + Thread.currentThread().getName());
        return "调用失败了";
    }


}
