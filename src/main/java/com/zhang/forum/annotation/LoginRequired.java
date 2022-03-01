package com.zhang.forum.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zhangshuo
 * @create 2022-02-28 11:37
 * @Description todo
 */
@Target(ElementType.METHOD)//作用在方法上
@Retention(RetentionPolicy.RUNTIME)//程序运行时才有效
public @interface LoginRequired {

}
