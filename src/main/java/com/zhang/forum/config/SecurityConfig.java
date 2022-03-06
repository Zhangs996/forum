package com.zhang.forum.config;

import com.zhang.forum.util.ForumConstant;
import com.zhang.forum.util.ForumUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter implements ForumConstant {

    @Override
//    对此路径不做检查
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/resources/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 授权
        // antMatchers：主要是看项目的所有的controller访问路径，可以把项目的所有的controller的路径过一遍
        // hasAnyAuthority：对于antMatchers的这些路径，拥有以下任一权限都可以访问antMatchers的路径
        // anyRequest().permitAll()：除了antMatchers的路径，就可以访问，不做安全检查

        http.authorizeRequests()
                .antMatchers(
                        "/user/setting",
                        "/user/upload",
                        "/discuss/add",
                        "/comment/add/**",
                        "/letter/**",
                        "/notice/**",
                        "/like",
                        "/follow",
                        "/unfollow"
                )
                .hasAnyAuthority(
                        AUTHORITY_USER,
                        AUTHORITY_ADMIN,
                        AUTHORITY_MODERATOR
                )
                .antMatchers(
                        "/discuss/top",
                        "/discuss/wonderful"
                )
                .hasAnyAuthority(
                        AUTHORITY_MODERATOR
                )
                .antMatchers(
                        "/discuss/delete",
                        "/data/**",
                        "/actuator/**"
                )
                .hasAnyAuthority(
                        AUTHORITY_ADMIN
                )
                .anyRequest().permitAll()
                .and().csrf().disable();//不启用csrf检查

        // 权限不够时的处理
        http.exceptionHandling()
                .authenticationEntryPoint(new AuthenticationEntryPoint()/*匿名实现一个接口*/ {
                    // 没有登录
                    @Override
                    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
                        String xRequestedWith = request.getHeader("x-requested-with");//得到这个对应的值
                        if ("XMLHttpRequest".equals(xRequestedWith)) {//如果是异步请求
                            response.setContentType("application/plain;charset=utf-8");//声明给浏览器响应的数据的类型
                            PrintWriter writer = response.getWriter();//给浏览器写数据，拒绝访问
                            writer.write(ForumUtil.getJSONString(403, "你还没有登录哦!"));
                        } else {//普通请求，拒绝访问，重定向
                            response.sendRedirect(request.getContextPath() + "/login");
                        }
                    }
                })
                .accessDeniedHandler(new AccessDeniedHandler() {
                    // 登录了，但是权限不足
                    @Override
                    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
                        String xRequestedWith = request.getHeader("x-requested-with");
                        if ("XMLHttpRequest".equals(xRequestedWith)) {
                            response.setContentType("application/plain;charset=utf-8");
                            PrintWriter writer = response.getWriter();
                            writer.write(ForumUtil.getJSONString(403, "你没有访问此功能的权限!"));
                        } else {
                            response.sendRedirect(request.getContextPath() + "/denied");
                        }
                    }
                });

        // Security底层默认会拦截/logout请求,进行退出处理.
        // 因为spring security的Filter会在dispatcherServlet之前拦截，肯定在controller之前，如果spring security拦截了logout的请求，那么就不会走我们自己写的logout
        // 现在我想执行我自己写的logout，而不是走spring security默认的退出处理
        // 覆盖它默认的逻辑(默认会拦截/logout),才能执行我们自己的退出代码.
        http.logout().logoutUrl("/securitylogout");//让spring security拦截/securitylogout路径，而不拦截/logout路径，其实并没有这个路径，只是一个欺骗
    }

}
