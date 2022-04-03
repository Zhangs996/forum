package com.zhang.forum.controller.interceptor;

import com.zhang.forum.annotation.LoginRequired;
import com.zhang.forum.util.HostHolder;
import com.zhang.forum.annotation.LoginRequired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Component
public class LoginRequiredInterceptor implements HandlerInterceptor {

    @Autowired
    private HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//       判断拦截的是不是方法还是静态路径，如果是方法那么进入
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
//            获得方法对象
            Method method = handlerMethod.getMethod();
//            尝试取这个注解
            LoginRequired loginRequired = method.getAnnotation(LoginRequired.class);//方法如果存在这样的注释，则返回指定类型的元素的注释，否则为null
//            loginRequired != null代表这个方法是需要登录才能访问的，也就是说这个方法加了自定义注解
            if (loginRequired != null && hostHolder.getUser() == null) {
//                项目名可以用配置文件得到也可以通过request拿到
                response.sendRedirect(request.getContextPath() + "/login");
                return false;
            }
        }
        return true;
    }
}
