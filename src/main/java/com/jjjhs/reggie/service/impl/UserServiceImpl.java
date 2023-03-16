package com.jjjhs.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjjhs.reggie.entity.User;
import com.jjjhs.reggie.mapper.UserMapper;
import com.jjjhs.reggie.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
