package com.example.demo.service.impl;


import com.example.demo.dao.UserMapper;
import com.example.demo.dto.User;
import com.example.demo.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;


    public Boolean add(User user){
        this.userMapper.insert(user);
        return Boolean.TRUE;
    }


}
