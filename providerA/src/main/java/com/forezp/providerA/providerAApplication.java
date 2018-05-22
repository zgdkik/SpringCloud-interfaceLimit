package com.forezp.providerA;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName providerAApplication
 * @Description 功能说明：
 * @Author 刘松
 * @Date 2018/5/3 11:27
 * @Version 1.0
 **/
@SpringBootApplication
@EnableEurekaClient
@RestController
public class providerAApplication {

    @Value("${server.port}")
    private String port;


    public static void main(String[] args) {
        SpringApplication.run(providerAApplication.class, args);
    }

    @RequestMapping(value = "/hi")
    public String hi(){
        return "我是providerA,我的端口号是："+port;
    }

}
