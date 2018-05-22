package com.icitic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @ClassName Application
 * @Description 功能说明：
 * @Author 刘松
 * @Date 2018/5/10 11:00
 * @Version 1.0
 **/
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@EnableHystrix
@RestController
public class Application {

    @Autowired
    private RestTemplate restTemplate;


    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

    }
    @RequestMapping("/say")
    public String sayHi(){
        long start = System.currentTimeMillis();
        initThread(10);
        long end = System.currentTimeMillis();

        return "执行完毕! 用时="+(end-start);
    }

    private  class TaskDemo implements Runnable{
        @Override
        public void run() {
            String result = restTemplate.getForObject("http://192.168.1.42:9999/method3",String.class);
            System.out.println(Thread.currentThread().getName()+" \t result="+result);
        }
    }


    private void initThread(int size){
        ExecutorService es= Executors.newFixedThreadPool(size);
        for(int i=0;i<size;i++){
            TaskDemo tc=new TaskDemo();
            es.execute(tc);
        }
        es.shutdown();
    }

}
