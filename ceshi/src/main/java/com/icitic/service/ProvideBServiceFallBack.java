package com.icitic.service;

import org.springframework.stereotype.Component;

/**
 * @ClassName ProvideBServiceFallBack
 * @Description 功能说明：
 * @Author 刘松
 * @Date 2018/5/19 23:51
 * @Version 1.0
 **/
@Component
public class ProvideBServiceFallBack implements ProvideBService {
    @Override
    public String test() {
        System.out.println("======================== findByIdFallback " + Thread.currentThread().getThreadGroup() + " - " + Thread.currentThread().getId() + " - " + Thread.currentThread().getName());
        return "调用失败了";
    }
}
