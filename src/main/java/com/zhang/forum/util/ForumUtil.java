package com.zhang.forum.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qiniu.util.Json;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ForumUtil {

    // 生成随机字符串
    // 每次上传文件生成随机字符串(比如图片)，所以提前封装好
    public static String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    // MD5加密，比如
    // hello 加密后-> abc123def456，它每次加密后的结果都是这个值，黑客会有一个密码库，容易被破解
    // 解决方法：不管你是什么密码。都加上一个随机字符串
    // hello + 3e4a8(随机字符串) -> 假设是abc123def456abc,黑客的库没有这样的字符串，由于字符串是随机的，破解的难度是很大的，加上中文破解难度更大
    public static String md5(String key) {
        //空格 空字符串都会被认为是空
        if (StringUtils.isBlank(key)) {
            return null;
        }
        //把密码转为16进制的字符串
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }

//    把json转为字符串,编号code一定有，业务数据msg不一定有，所以要重载getJSONString方法
    public static String getJSONString(int code, String msg, Map<String, Object> map) {
        JSONObject json = new JSONObject();

        json.put("code", code);
        json.put("msg", msg);
        if (map != null) {
            for (String key : map.keySet()) {
                json.put(key, map.get(key));
            }
        }
        return json.toJSONString();

    }

    public static String getJSONString(int code, String msg) {
        return getJSONString(code, msg, null);
    }

    public static String getJSONString(int code) {
        return getJSONString(code, null, null);
    }

    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "zhangsan");
        map.put("age", 25);
        System.out.println(getJSONString(0, "ok", map));//{"msg":"ok","code":0,"name":"zhangsan","age":25}
    }

}
