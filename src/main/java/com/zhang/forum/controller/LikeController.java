package com.zhang.forum.controller;

import com.zhang.forum.entity.Event;
import com.zhang.forum.entity.User;
//import com.zhang.forum.event.EventProducer;
import com.zhang.forum.service.LikeService;
import com.zhang.forum.util.ForumConstant;
import com.zhang.forum.util.ForumUtil;
import com.zhang.forum.util.HostHolder;
import com.zhang.forum.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class LikeController implements ForumConstant {

    @Autowired
    private LikeService likeService;

    @Autowired
    private HostHolder hostHolder;

//    @Autowired
//    private EventProducer eventProducer;

    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping(path = "/like", method = RequestMethod.POST)
    @ResponseBody//异步请求得加这个
    //执行点赞的操作
    public String like(int entityType, int entityId, int entityUserId, int postId) {
        User user = hostHolder.getUser();

        // 点赞
        likeService.like(user.getId(), entityType, entityId, entityUserId);

        // 赞的数量
        long likeCount = likeService.findEntityLikeCount(entityType, entityId);
        // 查询某人对某实体的点赞状态
        int likeStatus = likeService.findEntityLikeStatus(user.getId(), entityType, entityId);
        // 返回的结果存在map里，统一传给页面
        Map<String, Object> map = new HashMap<>();
        map.put("likeCount", likeCount);
        map.put("likeStatus", likeStatus);

//        // 触发点赞事件
//        if (likeStatus == 1) {
//            Event event = new Event()
//                    .setTopic(TOPIC_LIKE)
//                    .setUserId(hostHolder.getUser().getId())
//                    .setEntityType(entityType)
//                    .setEntityId(entityId)
//                    .setEntityUserId(entityUserId)
//                    .setData("postId", postId);
//            eventProducer.fireEvent(event);
//        }
//
//        if(entityType == ENTITY_TYPE_POST) {
//            // 计算帖子分数
//            String redisKey = RedisKeyUtil.getPostScoreKey();
//            redisTemplate.opsForSet().add(redisKey, postId);
//        }

        return ForumUtil.getJSONString(0, null, map);
    }

}
