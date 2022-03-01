package com.zhang.forum.controller.advice;

import com.zhang.forum.util.ForumUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@ControllerAdvice(annotations = Controller.class)//只扫描带有@controller的组件,带有@controller的组件都能被扫描到
public class ExceptionAdvice {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionAdvice.class);

    @ExceptionHandler({Exception.class})
    //最常见的就是这三个参数
    public void handleException(Exception e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.error("服务器发生异常: " + e.getMessage());
//        取每个异常
        for (StackTraceElement element : e.getStackTrace()) {
            logger.error(element.toString());
        }

//        要看请求是普通请求还是异步请求
        String xRequestedWith = request.getHeader("x-requested-with");//获得请求方式
        if ("XMLHttpRequest".equals(xRequestedWith)) {//这是异步请求
            response.setContentType("application/plain;charset=utf-8");//这个时候就需要服务器给浏览器响应一个字符串了，浏览器会自动把字符串转为json
            PrintWriter writer = response.getWriter();//获得输出流
            writer.write(ForumUtil.getJSONString(1, "服务器异常!"));//把json转为字符串
        } else {//如果是普通请求，就会重定向到错误页面
            response.sendRedirect(request.getContextPath() + "/error");
        }
    }

}
