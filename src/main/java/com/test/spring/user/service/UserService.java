package com.test.spring.user.service;

import com.test.spring.user.vo.User;

import java.util.List;

public interface UserService {

    Integer save(User user);
    boolean update(User user);

    List<User> findAllUser();
}
