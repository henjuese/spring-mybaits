package com.test.spring.user.dao;

import com.test.spring.user.vo.User;

import java.util.List;

public interface UserDaoMapper {

    int save(User user);
    boolean update(User user);
    List<User> findAll();

}
