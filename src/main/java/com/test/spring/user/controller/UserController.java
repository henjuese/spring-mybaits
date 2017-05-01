package com.test.spring.user.controller;

import com.test.spring.user.service.UserService;
import com.test.spring.user.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;


    @RequestMapping("/addUser")
    @ResponseBody
    public String addUser() {
        User user = new User();
        user.setAge("11");
        user.setUserName("baowenwei");
        userService.save(user);
        return "success";
    }

    @RequestMapping("/find")
    @ResponseBody
    public List<User> findAll() {
        return userService.findAllUser();
    }


}
