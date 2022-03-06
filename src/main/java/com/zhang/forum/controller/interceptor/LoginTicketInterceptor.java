package com.zhang.forum.controller.interceptor;

import com.zhang.forum.entity.LoginTicket;
import com.zhang.forum.entity.User;
import com.zhang.forum.service.UserService;
import com.zhang.forum.util.CookieUtil;
import com.zhang.forum.util.HostHolder;
import com.zhang.forum.entity.LoginTicket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Component
public class LoginTicketInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;//持有用户信息,用于代替session对象

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 从cookie中获取凭证
        String ticket = CookieUtil.getValue(request, "ticket");

        if (ticket != null) {
            // 查询凭证
            LoginTicket loginTicket = userService.findLoginTicket(ticket);
            // 检查凭证是否有效
            if (loginTicket != null && loginTicket.getStatus() == 0 && loginTicket.getExpired().after(new Date())) {
                // 根据凭证查询用户
                User user = userService.findUserById(loginTicket.getUserId());
                // 在本次请求中持有用户
                // 浏览器访问服务器是多对一的关系，每个浏览器访问服务器，服务器会创建一个独立的线程来处理请求
                // 所以服务器处理这个请求的时候要考虑多线程的情况，如果只是存在一个变量中，在并发的情况下会出现问题
                // 所以要考虑线程的隔离，每个线程都存一份，互相不干扰

                //只要这个请求没有处理完，这个线程就会一直存在在ThreadLocal
                hostHolder.setUser(user);

//                System.out.println(loginTicket.getTicket());
//                System.out.println("preHandle  "+redisTemplate.opsForValue().get(loginTicket.getTicket()));
                // 构建用户认证的结果,并存入SecurityContext(SecurityContext以SecurityContextHolder实现),以便于Security进行授权.
                // 如果是用户密码的认证方式，那就是UsernamePasswordAuthenticationToken
                // 第三个参数是权限
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        user, user.getPassword(), userService.getAuthorities(user.getId()));
                SecurityContextHolder.setContext(new SecurityContextImpl(authentication));
            }
        }

        return true;
    }

//在模板引擎调用之前，把user存到modelAndView中
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        User user = hostHolder.getUser();
        if (user != null && modelAndView != null) {
            modelAndView.addObject("loginUser", user);
        }

    }

//    在整个请求结束后清理hostHolder
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        hostHolder.clear();
//        SecurityContextHolder.clearContext();
//        System.out.println("afterCompletion  "+hostHolder.getUser());
    }
}
