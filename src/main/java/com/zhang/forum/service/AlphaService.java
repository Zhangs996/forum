package com.zhang.forum.service;

import com.zhang.forum.dao.AlphaDao;
import com.zhang.forum.dao.DiscussPostMapper;
import com.zhang.forum.dao.UserMapper;
import com.zhang.forum.entity.DiscussPost;
import com.zhang.forum.entity.User;
import com.zhang.forum.util.ForumUtil;
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
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private DiscussPostMapper discussPostMapper;
    @Autowired
    private TransactionTemplate transactionTemplate;

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


    // 声明式事务 通过注解，声明某方法的事务特征。
    // propagation是事务的传播机制，就是说业务A调用B，A 和 B都有注解，都会管理事务，那么以谁为准?
    // 它有7个值，挑下面常见的三个注释
    // REQUIRED: 支持当前事务(外部事务),如果不存在则创建新事务.以A为准
    // REQUIRES_NEW: 创建一个新事务,并且暂停当前事务(外部事务).如果A不存在，则永远创建一个新事务
    // NESTED: 如果当前存在事务(外部事务),则B嵌套在该事务(A)中执行(独立的提交和回滚),如果外部事务不存在就会和REQUIRED一样.就是说B有独立的回滚和提交，B嵌套在A中执行？这个不理解
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public Object save1(){
        // 新增用户
        User user = new User();
        user.setUsername("alpha");
        user.setSalt(ForumUtil.generateUUID().substring(0, 5));
        user.setPassword(ForumUtil.md5("123" + user.getSalt()));
        user.setEmail("alpha@qq.com");
        user.setHeaderUrl("http://image.nowcoder.com/head/99t.png");
        user.setCreateTime(new Date());
        userMapper.insertUser(user);

        // 新增帖子
        DiscussPost post = new DiscussPost();
        post.setUserId(user.getId());
        post.setTitle("Hello");
        post.setContent("新人报道!");
        post.setCreateTime(new Date());
        discussPostMapper.insertDiscussPost(post);

//      人为造个错，看看能不能回滚
        Integer.valueOf("abc");

        return "ok";
   }

   //编程式事务 通过 TransactionTemplate 管理事务，并通过它执行数据库的操作。
    public Object save2() {
        transactionTemplate.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

        // 回调方法
        return transactionTemplate.execute(new TransactionCallback<Object>() {
            @Override
            public Object doInTransaction(TransactionStatus status) {
                // 新增用户
                User user = new User();
                user.setUsername("beta");
                user.setSalt(ForumUtil.generateUUID().substring(0, 5));
                user.setPassword(ForumUtil.md5("123" + user.getSalt()));
                user.setEmail("beta@qq.com");
                user.setHeaderUrl("http://image.nowcoder.com/head/999t.png");
                user.setCreateTime(new Date());
                userMapper.insertUser(user);

                // 新增帖子
                DiscussPost post = new DiscussPost();
                post.setUserId(user.getId());
                post.setTitle("你好");
                post.setContent("我是新人!");
                post.setCreateTime(new Date());
                discussPostMapper.insertDiscussPost(post);

                Integer.valueOf("abc");

                return "ok";
            }
        });
    }
    // 让该方法在多线程环境下,被异步的调用.
    @Async
    public void execute1() {
        logger.debug("execute1");
    }

    /*@Scheduled(initialDelay = 10000, fixedRate = 1000)*/
    public void execute2() {
        logger.debug("execute2");
    }

}
