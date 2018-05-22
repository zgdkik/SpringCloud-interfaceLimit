package com.yasaka.stock;

import com.yasaka.stock.filter.AccessFilter;
import com.yasaka.stock.filter.RateLimitZuulFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

@SpringCloudApplication
@EnableZuulProxy
public class ZuulApplication {
    public static void main(String[] args) {
        SpringApplication.run(ZuulApplication.class, args);
    }

    @Bean
    public RateLimitZuulFilter accessRateLimitZuulFilter(){
        return new RateLimitZuulFilter();
    }

    @Bean
    public AccessFilter accessAccessFilter(){
        return new AccessFilter();
    }

}