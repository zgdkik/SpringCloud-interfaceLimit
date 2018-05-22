package com.icitic.controller;

import com.google.common.util.concurrent.RateLimiter;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @ClassName MovieRibbonHystrixPropagationController
 * @Description 功能说明：
 * @Author 刘松
 * @Date 2018/5/18 15:11
 * @Version 1.0
 **/
@RestController
public class MovieRibbonHystrixPropagationController {

    @Autowired
    private RestTemplate restTemplate;

    private String url = "http://service-b/hi";

    private int requestCount = 2;

    private  RateLimiter rateLimiter = RateLimiter.create(requestCount);

    @RequestMapping("/setLimiter")
    public void setRateLimiterParam(@RequestParam(value = "requestCount") int requestCount,
                                    @RequestParam(value = "url") String url){
        this.requestCount = requestCount;
        this.url = url;
        rateLimiter = RateLimiter.create(requestCount-1);
        System.out.println("setLimiter方法执行了! requestCount="+requestCount+" url="+url);
    }



    @RequestMapping("/method1")
    @HystrixCommand(fallbackMethod = "findByIdFallback",
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE")
            })
    public String sayHello() throws Exception{
        if (!rateLimiter.tryAcquire()) {
            throw new Exception("限流了!!!!");

        }
        return restTemplate.getForObject(url,String.class);
    }

    public String findByIdFallback() {
        System.out.println("======================== findByIdFallback " + Thread.currentThread().getThreadGroup() + " - " + Thread.currentThread().getId() + " - " + Thread.currentThread().getName());
        return "调用失败了";
    }
}
