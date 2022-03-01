package com.zhang.forum.controller;

import com.zhang.forum.entity.Comment;
import com.zhang.forum.entity.DiscussPost;
import com.zhang.forum.entity.Event;
//import com.zhang.forum.event.EventProducer;
import com.zhang.forum.service.CommentService;
import com.zhang.forum.service.DiscussPostService;
import com.zhang.forum.util.ForumConstant;
import com.zhang.forum.util.HostHolder;
import com.zhang.forum.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;

@Controller
@RequestMapping("/comment")
public class CommentController implements ForumConstant {

    @Autowired
    private CommentService commentService;

    @Autowired
    private HostHolder hostHolder;

//    @Autowired
//    private EventProducer eventProducer;

    @Autowired
    private DiscussPostService discussPostService;

//    @Autowired
//    private RedisTemplate redisTemplate;

    @RequestMapping(path = "/add/{discussPostId}", method = RequestMethod.POST)
    public String addComment(@PathVariable("discussPostId") int discussPostId, Comment comment) {
        comment.setUserId(hostHolder.getUser().getId());
        comment.setStatus(0);
        comment.setCreateTime(new Date());
        commentService.addComment(comment);

        // 触发评论事件
//        Event event = new Event()
//                .setTopic(TOPIC_COMMENT)
//                .setUserId(hostHolder.getUser().getId())
//                .setEntityType(comment.getEntityType())
//                .setEntityId(comment.getEntityId())
//                .setData("postId", discussPostId);
//        if (comment.getEntityType() == ENTITY_TYPE_POST) {
//            DiscussPost target = discussPostService.findDiscussPostById(comment.getEntityId());
//            event.setEntityUserId(target.getUserId());
//        } else if (comment.getEntityType() == ENTITY_TYPE_COMMENT) {
//            Comment target = commentService.findCommentById(comment.getEntityId());
//            event.setEntityUserId(target.getUserId());
//        }
//        eventProducer.fireEvent(event);
//
//        if (comment.getEntityType() == ENTITY_TYPE_POST) {
//            // 触发发帖事件
//            event = new Event()
//                    .setTopic(TOPIC_PUBLISH)
//                    .setUserId(comment.getUserId())
//                    .setEntityType(ENTITY_TYPE_POST)
//                    .setEntityId(discussPostId);
//            eventProducer.fireEvent(event);
//            // 计算帖子分数
//            String redisKey = RedisKeyUtil.getPostScoreKey();
//            redisTemplate.opsForSet().add(redisKey, discussPostId);
//        }

        return "redirect:/discuss/detail/" + discussPostId;
    }

}
