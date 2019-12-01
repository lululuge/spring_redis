package com.luge;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.luge.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

@SpringBootTest(classes = SpringRedisApplication.class)
public class TestRedis {
    @Autowired
    private UserDao userDao;

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void test() throws JsonProcessingException {
        String json = (String) redisTemplate.boundValueOps("users").get();
        // 判断是否为空
        if (json == null) {
            // 从数据库中获取数据
            System.out.println("-----从数据库中获取数据-----");
            List<User> users = userDao.findAll();
            // 存储数据到缓存中
            // 把列表数据转为json类型
            ObjectMapper mapper = new ObjectMapper();
            json = mapper.writeValueAsString(users);
            redisTemplate.boundValueOps("users").set(json);
        } else {
            System.out.println("-----从缓存中获取数据-----");
        }
        System.out.println(json);
    }
}
