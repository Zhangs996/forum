package com.zhang.forum.entity;

import java.util.HashMap;
import java.util.Map;

public class Event {

    // 事件的类型就是topic
    private String topic;

    // 事件触发的人
    private int userId;

    // 事件发生在哪个实体身上
    private int entityType;

    // 实体的id
    private int entityId;

    // 实体的作者
    private int entityUserId;

    // 冗余字段，具有一定的扩展性
    private Map<String, Object> data = new HashMap<>();

    public String getTopic() {
        return topic;
    }

    public Event setTopic(String topic) {
        this.topic = topic;
        return this;//代替了有参构造器，比较灵活
    }

    public int getUserId() {
        return userId;
    }

    public Event setUserId(int userId) {
        this.userId = userId;
        return this;
    }

    public int getEntityType() {
        return entityType;
    }

    public Event setEntityType(int entityType) {
        this.entityType = entityType;
        return this;
    }

    public int getEntityId() {
        return entityId;
    }

    public Event setEntityId(int entityId) {
        this.entityId = entityId;
        return this;
    }

    public int getEntityUserId() {
        return entityUserId;
    }

    public Event setEntityUserId(int entityUserId) {
        this.entityUserId = entityUserId;
        return this;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public Event setData(String key, Object value) {
        this.data.put(key, value);
        return this;
    }

}
