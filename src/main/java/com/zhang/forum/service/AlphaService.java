package com.zhang.forum.service;

import com.zhang.forum.dao.AlphaDao;
import com.zhang.forum.entity.DiscussPost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Date;

@Service

//使该类成为多例的（默认是单例的，只能getBean一次）
//@Scope("prototype")
public class AlphaService {

    @Autowired
    private AlphaDao alphaDao;

    private static final Logger logger = LoggerFactory.getLogger(AlphaService.class);
    public AlphaService(){
        System.out.println(AlphaService.class.getName()+"实例化");
    }
    //PostConstruct使得初始化方法在构造器之后调用
    @PostConstruct
    public void init(){
        System.out.println("AlphaService初始化");
    }

    ////PostConstruct使得销毁方法在该类销毁之前调用
    @PreDestroy
    public void destroy(){
        System.out.println("AlphaService销毁");
    }

    //Service依赖Dao的方式
    public String find(){
        return alphaDao.select();
    }

}
