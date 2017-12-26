package com.example.springbootdemo.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class RedisService {

    @Resource
    public RedisTemplate redisTemplate;

    @Resource
    public StringRedisTemplate stringRedisTemplate;

    @Resource(name="stringRedisTemplate")
    public ValueOperations<String,String> strValOps;

    @Resource(name="redisTemplate")
    public ValueOperations<String,Object> objValOps;

    @Resource(name="redisTemplate")
    public HashOperations<String,String,Object> hashObjValOps;

    @CachePut(value="testallCache")
    public String addString(String key, String value){
         strValOps.set(key, value);
        return value;
    }
    @Cacheable(value="testallCache")
    public String getString(String key){
        System.out.println("getString");
        return strValOps.get(key);
    }

    public void addObject(User user){
        objValOps.set(user.getId(), user);
    }

    public User getObject(String key){
        return (User)objValOps.get(key);
    }

    public void addHashObject(User user){
        //objValOps.set(user.getId(), user);
        hashObjValOps.put(user.getId(), user.getId(), user);

    }

    public User getHashObject(String key){
        //objValOps.set(user.getId(), user);
        return (User) hashObjValOps.get(key, key);

    }
}
