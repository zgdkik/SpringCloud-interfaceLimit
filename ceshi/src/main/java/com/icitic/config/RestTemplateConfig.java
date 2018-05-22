package com.icitic.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @ClassName RestTemplateConfig
 * @Description 功能说明：
 * @Author 刘松
 * @Date 2018/5/18 11:19
 * @Version 1.0
 **/
@Configuration
public class RestTemplateConfig {

    @Bean
//    @LoadBalanced
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

}
