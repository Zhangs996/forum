package com.zhang.forum.controller;

import com.zhang.forum.entity.*;
//import com.zhang.forum.event.EventProducer;
//import com.zhang.forum.service.CommentService;
import com.zhang.forum.service.CommentService;
import com.zhang.forum.service.DiscussPostService;
//import com.zhang.forum.service.LikeService;
import com.zhang.forum.service.UserService;
import com.zhang.forum.util.ForumConstant;
import com.zhang.forum.util.ForumUtil;
import com.zhang.forum.util.HostHolder;
import com.zhang.forum.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
@RequestMapping("/discuss")
public class DiscussPostController implements ForumConstant {

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;
//
//    @Autowired
//    private LikeService likeService;
//
//    @Autowired
//    private EventProducer eventProducer;
//
//    @Autowired
//    private RedisTemplate redisTemplate;

    //   请求方式是POST，只需要过滤title和content
    @RequestMapping(path = "/add", method = RequestMethod.POST)
    @ResponseBody
    public String addDiscussPost(String title, String content) {
        User user = hostHolder.getUser();
        if (user == null) {
//            返回一个数据，用异步方式实现
            return ForumUtil.getJSONString(403, "你还没有登录哦!");
        }

        DiscussPost post = new DiscussPost();
        post.setUserId(user.getId());
        post.setTitle(title);
        post.setContent(content);
        post.setCreateTime(new Date());
        discussPostService.addDiscussPost(post);

//        // 触发发帖事件
//        Event event = new Event()
//                .setTopic(TOPIC_PUBLISH)
//                .setUserId(user.getId())
//                .setEntityType(ENTITY_TYPE_POST)
//                .setEntityId(post.getId());
//        eventProducer.fireEvent(event);
//
//        // 计算帖子分数
//        String redisKey = RedisKeyUtil.getPostScoreKey();
//        redisTemplate.opsForSet().add(redisKey, post.getId());
//
        // 报错的情况,将来统一处理
        return ForumUtil.getJSONString(0, "发布成功!");
    }


    @RequestMapping(path = "/detail/{discussPostId}", method = RequestMethod.GET)
    public String getDiscussPost(@PathVariable("discussPostId") int discussPostId, Model model, Page page) {
        // 查帖子
        DiscussPost post = discussPostService.findDiscussPostById(discussPostId);
        model.addAttribute("post", post);
        // 查作者，下面两句为什么不用关联查询，可能是为了去耦合
        User user = userService.findUserById(post.getUserId());
        model.addAttribute("user", user);
        // 点赞数量
//        long likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_POST, discussPostId);
//        model.addAttribute("likeCount", likeCount);
        // 点赞状态
//        int likeStatus = hostHolder.getUser() == null ? 0 :
//                likeService.findEntityLikeStatus(hostHolder.getUser().getId(), ENTITY_TYPE_POST, discussPostId);
//        model.addAttribute("likeStatus", likeStatus);

        // 评论分页信息
        page.setLimit(5);
        page.setPath("/discuss/detail/" + discussPostId);
        //一共有多少条评论数据
        page.setRows(post.getCommentCount());//冗余字段

        // ENTITY_TYPE_POST=1是实体类型：帖子   COMMENT_TYPE_POST=2是实体类型：评论
        // 评论: 给帖子的评论
        // 回复: 给评论的评论
        // 当前帖子的所有评论
        List<Comment> commentList = commentService.findCommentsByEntity(
                ENTITY_TYPE_POST, post.getId(), page.getOffset(), page.getLimit());
        // 每条评论的所有回复的集合
        List<Map<String, Object>> commentVoList = new ArrayList<>();
        if (commentList != null) {
            for (Comment comment : commentList) {
                // 评论VO
                Map<String, Object> commentVo = new HashMap<>();
                // 评论
                commentVo.put("comment", comment);
                // 作者
                commentVo.put("user", userService.findUserById(comment.getUserId()));
                // 回复列表
                List<Comment> replyList = commentService.findCommentsByEntity(
                        ENTITY_TYPE_COMMENT, comment.getId(), 0, Integer.MAX_VALUE);
                // 回复VO列表
                List<Map<String, Object>> replyVoList = new ArrayList<>();
                if (replyList != null) {
                    for (Comment reply : replyList) {
                        Map<String, Object> replyVo = new HashMap<>();
                        // 回复
                        replyVo.put("reply", reply);
                        // 作者
                        replyVo.put("user", userService.findUserById(reply.getUserId()));
                        // 回复目标
                        // targetId只发生在回复里
                        User target = reply.getTargetId() == 0 ? null : userService.findUserById(reply.getTargetId());

                        replyVo.put("target", target);
//                        // 点赞数量
//                        likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_COMMENT, reply.getId());
//                        replyVo.put("likeCount", likeCount);
//                        // 点赞状态
//                        likeStatus = hostHolder.getUser() == null ? 0 :
//                                likeService.findEntityLikeStatus(hostHolder.getUser().getId(), ENTITY_TYPE_COMMENT, reply.getId());
//                        replyVo.put("likeStatus", likeStatus);

                        replyVoList.add(replyVo);
                    }
                }
                commentVo.put("replys", replyVoList);

                // 回复数量
                int replyCount = commentService.findCommentCount(ENTITY_TYPE_COMMENT, comment.getId());
                commentVo.put("replyCount", replyCount);
                commentVoList.add(commentVo);
            }
        }

        model.addAttribute("comments", commentVoList);
        return "/site/discuss-detail";
//        return "site/test";
    }


//    // 置顶
//    @RequestMapping(path = "/top", method = RequestMethod.POST)
//    @ResponseBody
//    public String setTop(int id) {
//        discussPostService.updateType(id, 1);
//
//        // 触发发帖事件
//        Event event = new Event()
//                .setTopic(TOPIC_PUBLISH)
//                .setUserId(hostHolder.getUser().getId())
//                .setEntityType(ENTITY_TYPE_POST)
//                .setEntityId(id);
//        eventProducer.fireEvent(event);
//
//        return forumUtil.getJSONString(0);
//    }
//
//    // 加精
//    @RequestMapping(path = "/wonderful", method = RequestMethod.POST)
//    @ResponseBody
//    public String setWonderful(int id) {
//        discussPostService.updateStatus(id, 1);
//
//        // 触发发帖事件
//        Event event = new Event()
//                .setTopic(TOPIC_PUBLISH)
//                .setUserId(hostHolder.getUser().getId())
//                .setEntityType(ENTITY_TYPE_POST)
//                .setEntityId(id);
//        eventProducer.fireEvent(event);
//
//        // 计算帖子分数
//        String redisKey = RedisKeyUtil.getPostScoreKey();
//        redisTemplate.opsForSet().add(redisKey, id);
//
//        return forumUtil.getJSONString(0);
//    }
//
//    // 删除
//    @RequestMapping(path = "/delete", method = RequestMethod.POST)
//    @ResponseBody
//    public String setDelete(int id) {
//        discussPostService.updateStatus(id, 2);
//
//        // 触发删帖事件
//        Event event = new Event()
//                .setTopic(TOPIC_DELETE)
//                .setUserId(hostHolder.getUser().getId())
//                .setEntityType(ENTITY_TYPE_POST)
//                .setEntityId(id);
//        eventProducer.fireEvent(event);
//
//        return forumUtil.getJSONString(0);
//    }

}

