package com.zhang.forum.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

//@Component
//@Aspect
public class AlphaAspect {

    //定义切点，表示service的所有组件（.*）的所有方法（.*）的所有参数（(..)）都要处理
    @Pointcut("execution(* com.zhang.forum.service.*.*(..))")
    public void pointcut() {

    }

    //有了切点后，就要定义通知
    @Before("pointcut()")
    public void before() {
        System.out.println("before");
    }

    @After("pointcut()")
    public void after() {
        System.out.println("after");
    }

    @AfterReturning("pointcut()")
    public void afterRetuning() {
        System.out.println("afterRetuning");
    }

    @AfterThrowing("pointcut()")
    public void afterThrowing() {
        System.out.println("afterThrowing");
    }

    @Around("pointcut()")
//    ProceedingJoinPoint是连接点
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("around before");
        Object obj = joinPoint.proceed();//目标连接点方法被调用了，返回的是连接点的返回值
        System.out.println("around after");
        return obj;
    }

}
