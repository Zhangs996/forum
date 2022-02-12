package com.zhang.forum;

import com.zhang.forum.dao.AlphaDao;
import com.zhang.forum.service.AlphaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;

import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootTest

//以该类的配置为测试配置
@ContextConfiguration(classes = ForumApplication.class)

//如何得到spring容器-->实现ApplicationContextAware接口
class ForumApplicationTests implements ApplicationContextAware {

    //成员变量，记录一下加载的容器
    private ApplicationContext applicationContext;
    @Test
    void contextLoads() {
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    //测试AlphaDao
    @Test
    public void testApplicationContext(){
        System.out.println(applicationContext);

        //getBean得到的object转型成AlphaDao.class
        AlphaDao alphaDao = applicationContext.getBean(AlphaDao.class);
        System.out.println(alphaDao.select());//MyBatis

        //getBean得到的object转型成AlphaDao.class
        alphaDao = applicationContext.getBean("alphaHibernate",AlphaDao.class);
    }

    //测试AlphaService
    @Test
    public void testBeanManager(){
        //被spring管理的实例默认是单例的
        AlphaService alphaService = applicationContext.getBean(AlphaService.class);
        System.out.println(alphaService);
        alphaService = applicationContext.getBean(AlphaService.class);
        System.out.println(alphaService);
    }
    //测试BeanConfig
    @Test
    public void testBeanConfig(){
        SimpleDateFormat simpleDateFormat =
                applicationContext.getBean(SimpleDateFormat.class);
        System.out.println(simpleDateFormat.format(new Date()));
    }

    //不再用applicationContext.getBean得到bean

    @Autowired
    //@Qualifier("alphaHibernate")//希望注入的不是AlphaDaoMyBatisImpl，想注入AlphaHibernate
    private AlphaDao alphaDao;
    @Autowired
    private AlphaService alphaService;
    @Autowired
    private SimpleDateFormat simpleDateFormat;


    //测试依赖注入
    @Test
    public void testDi(){
        System.out.println(alphaService);
    }
}
