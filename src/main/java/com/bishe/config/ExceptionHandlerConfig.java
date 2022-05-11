package com.bishe.config;


import cn.dev33.satoken.exception.NotLoginException;
import com.bishe.util.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 全局异常处理配置文件
 * @date 2022/2/18 15:39
 */
@ControllerAdvice
public class ExceptionHandlerConfig {
    /**
     * 自定义异常类处理
     * @param response
     * @param e
     */
    @ExceptionHandler(value = EcoBootException.class)
    private void ecoBootExceptionHandler(HttpServletResponse response, EcoBootException e){
        response.setCharacterEncoding("utf-8");
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            writer.write(Result.error(e.getMessage()).toString());
            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 未登录异常处理
     * @param response
     */

    @ExceptionHandler(value = NotLoginException.class)
    private void notLoginExceptionHandler(HttpServletResponse response){
        response.setCharacterEncoding("utf-8");
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            writer.write(Result.error("未登录！").toString());
            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}