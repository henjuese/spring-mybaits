package com.test.spring.user.service.impl;

import com.test.spring.user.service.UserService;
import com.test.spring.user.vo.User;
import junit.framework.TestCase;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-common.xml"})

public class UserServiceImplTest extends TestCase {


//    @Autowired
//    private UserService userService;
//
//    public void testSave() throws Exception {
//        User user = new User();
//        user.setAge("11");
//        user.setUserName("baowenwei");
//        int id = userService.save(user);
//        assertThat(id,greaterThan(1));
//    }
}