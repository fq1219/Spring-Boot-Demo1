package com.example.springbootdemo.redis;

import com.example.springbootdemo.DynamicDatasource.DynamicDatasourceType;
import com.example.springbootdemo.DynamicDatasource.TargetDatasource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
public class RedisController {

    @Resource
    public RedisService redisService;

    @RequestMapping("/add")
    @TargetDatasource(DynamicDatasourceType.MASTER)
    public String addString(String key, String value){
        redisService.addString(key,value);
        return "ok";
    }

    @RequestMapping("/get")
    public String getString(String key, HttpServletRequest request){
        //return redisService.getString(key);
        return request.getSession().getId();
    }

    @RequestMapping("/addUser")
    public String addUser(String key, String value){
        redisService.addObject(new User(key,value));
        return "ok";
    }
    @RequestMapping("/getUser")
    public User getUser(String key, String value){
        return redisService.getObject(key);
    }

    @RequestMapping("/addHashObject")
    public String addHashObject(String key, String value){
        redisService.addHashObject(new User(key,value));
        return "ok";
    }

    @RequestMapping("/getHashObject")
    public User getHashObject(String key, String value){
        return redisService.getHashObject(key);
    }
}
