package com.zhang.forum;

import com.zhang.forum.dao.DiscussPostMapper;
import com.zhang.forum.dao.LoginTicketMapper;
import com.zhang.forum.dao.MessageMapper;
import com.zhang.forum.dao.UserMapper;
import com.zhang.forum.entity.DiscussPost;
import com.zhang.forum.entity.LoginTicket;
import com.zhang.forum.entity.Message;
import com.zhang.forum.entity.User;
//不是import org.junit.jupiter.api.Test;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sound.midi.Soundbank;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

//通过@RunWith 和 @SpringBootTest启动spring容器。不加Runwith就无法得到@Mapper注释的UserMapper
//标准测试类里是要有@RunWith的，作用是告诉java你这个类通过用什么运行环境运行，例如启动和创建spring的应用上下文。
@RunWith(SpringRunner.class)
@SpringBootTest

//以该类的配置为测试配置
@ContextConfiguration(classes = ForumApplication.class)
public class MapperTests {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private LoginTicketMapper loginTicketMapper;

    @Autowired
    private MessageMapper messageMapper;
    @Test
    public void testSelectUser() {
        System.out.println(userMapper);
        List<User> users = userMapper.selectAll();
        for(User user:users){
            System.out.println(user);
        }
        System.out.println(users.size());
        User user = userMapper.selectById(101);
        System.out.println(user);

        user = userMapper.selectByName("SYSTEM");
        System.out.println(user);
        user = userMapper.selectByName("zhang");
        System.out.println(user);

        user = userMapper.selectByEmail("643396092@qq.com");
        System.out.println(user);
    }

    @Test
    public void testInsertUser() {
        User user = new User();
        user.setUsername("test");
        user.setPassword("123456");
        user.setSalt("abc");
        user.setEmail("test@qq.com");
        user.setHeaderUrl("http://www.qbnuzs12.com/102.png");
        user.setCreateTime(new Date());

        int rows = userMapper.insertUser(user);
        System.out.println(rows);
        System.out.println(user.getId());
    }

    @Test
    public void updateUser() {
        int rows = userMapper.updateStatus(166, 1);
        System.out.println(rows);

        rows = userMapper.updateHeader(166, "http://www.qbnuzs12.com/102.png");
        System.out.println(rows);

        rows = userMapper.updatePassword(166, "hello");
        System.out.println(rows);
    }

    @Test
    public void testSelectPosts() {
        List<DiscussPost> list = discussPostMapper.selectDiscussPosts(0, 0, 10,0);
        for(DiscussPost post : list) {
            System.out.println(post);
        }

        int rows = discussPostMapper.selectDiscussPostRows(149);
        System.out.println(rows);
    }

    @Test
    public void testInsertLoginTicket(){
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(101);
        loginTicket.setTicket("abc");
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis()+1000*60*60));
        loginTicketMapper.insertLoginTicket(loginTicket);
    }
    @Test
    public void testSelectLoginTicket() {
        LoginTicket loginTicket = loginTicketMapper.selectByTicket("abc");
        System.out.println(loginTicket);

        loginTicketMapper.updateStatus("abc", 1);
        loginTicket = loginTicketMapper.selectByTicket("abc");
        System.out.println(loginTicket.toString());
    }
    @Test
    public void testSelectLetters(){
        List<Message> list = messageMapper.selectConversations(111, 0, 12);
        for(Message message:list){
            System.out.println(message);
        }
    }
}
