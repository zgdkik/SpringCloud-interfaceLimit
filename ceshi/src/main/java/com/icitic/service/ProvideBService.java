package com.icitic.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @ClassName ProvideBService
 * @Description 功能说明：
 * @Author 刘松
 * @Date 2018/5/17 14:52
 * @Version 1.0
 **/
@FeignClient(value="service-b",fallback = ProvideBServiceFallBack.class)
public interface ProvideBService {
    @RequestMapping(value = "/hi")
    String test();
}
