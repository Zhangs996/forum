package com.zhang.forum.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

//@Component
//@Aspect//定义切面
public class AlphaAspect {

    //定义切点，表示service的所有组件（.*）的所有方法（.*）的所有参数（(..)）都要处理
    @Pointcut("execution(* com.zhang.forum.service.*.*(..))")
    public void pointcut() {

    }

    //有了切点后，就要定义通知
    @Before("pointcut()")//1.@Before 前置增强（目标方法执行之前，执行注解标注的内容）
    public void before() {
        System.out.println("before");
    }

    @After("pointcut()")//
    public void after() {
        System.out.println("after");
    }

    @AfterReturning("pointcut()")//@AfterReturning 后置增强（目标方法正常执行完毕后，执行）
    public void afterRetuning() {
        System.out.println("afterRetuning");
    }

    @AfterThrowing("pointcut()")//@AfterThrowing 抛出增强（目标方法发生异常，执行）
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
