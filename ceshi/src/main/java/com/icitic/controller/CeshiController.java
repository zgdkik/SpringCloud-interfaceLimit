package com.icitic.controller;

import com.google.common.util.concurrent.RateLimiter;
import com.icitic.service.ProvideBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName CeshiController
 * @Description 功能说明：
 * @Author 刘松
 * @Date 2018/5/16 17:49
 * @Version 1.0
 **/
@RestController
public class CeshiController {

    @Autowired
    private ProvideBService provideBService;

    private RateLimiter rateLimiter = null;

    @RequestMapping("/setLimiter2")
    public void setRateLimiterParam(@RequestParam(value = "requestCount") int requestCount){
        if(requestCount <=0){
            rateLimiter = null;
            return;
        }
        rateLimiter = RateLimiter.create(requestCount-1);
        System.out.println("setLimiter方法执行了! requestCount="+requestCount+" provideBService.test()方法执行了!");
    }


    @RequestMapping(value = "/method2")
    public String hi(){
        if(rateLimiter != null) {
            if (!rateLimiter.tryAcquire()) {
                return "/method2接口被限流了!";
            }
        }

        String result = provideBService.test();
        return result;
    }


}

