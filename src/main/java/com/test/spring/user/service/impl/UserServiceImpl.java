package com.test.spring.user.service.impl;

import com.test.spring.user.dao.UserDaoMapper;
import com.test.spring.user.service.UserService;
import com.test.spring.user.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserServiceImpl implements UserService {


    @Autowired
    private UserDaoMapper userDaoMapper;

    public Integer save(User user) {
        return userDaoMapper.save(user);
    }

    public boolean update(User user) {
        return userDaoMapper.update(user);
    }

    public List<User> findAllUser() {
        return userDaoMapper.findAll();
    }
}
