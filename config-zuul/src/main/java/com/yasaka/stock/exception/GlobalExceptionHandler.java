package com.yasaka.stock.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * @ClassName GlobalExceptionHandler
 * @Description 功能说明：统一异常处理
 * @Author 刘松
 * @Date 2018/5/10 16:29
 * @Version 1.0
 **/
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public String handleError1(Exception e, RedirectAttributes redirectAttributes) {
//        redirectAttributes.addFlashAttribute("message", e.getCause().getMessage());
//                return "redirect:/uploadStatus";

        System.out.printf("异常信息"+e.getMessage());
        return "";

    }
}
