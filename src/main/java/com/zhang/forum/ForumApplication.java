package com.zhang.forum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

//该注解标识的类表示该类是springboot启动类，表明是springboot程序的入口
@SpringBootApplication
public class ForumApplication {

    @PostConstruct//用来管理bean的初始化，由这个注解修饰的方法会在构造器被调用之后被执行
    public void init(){
        // 解决Netty冲突问题
        // see Netty4Utils
        System.setProperty("es.set.netty.runtime.available.processors","false");
    }
    public static void main(String[] args) {
        SpringApplication.run(ForumApplication.class, args);
    }

}
