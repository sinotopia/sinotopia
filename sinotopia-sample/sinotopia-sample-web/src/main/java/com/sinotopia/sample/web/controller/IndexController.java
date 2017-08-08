package com.sinotopia.sample.web.controller;

import com.sinotopia.common.base.BaseController;
import com.sinotopia.sample.rpc.api.SampleService;
import com.sinotopia.sample.web.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * 测试controller
 * Created by sinotopia on 2017/3/21.
 */
@Controller
public class IndexController extends BaseController {

    private static Logger _log = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    private SampleService sampleService;

    /**
     * jsp视图
     *
     * @return
     */
    @RequestMapping(value = "/jsp", method = RequestMethod.GET)
    public String jsp() {
        return jsp("/index");
    }

    /**
     * thymeleaf视图
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/thymeleaf", method = RequestMethod.GET)
    public String thymeleaf(Model model) {

        model.addAttribute("host", sampleService.sayHello("http://www.zhangshuzheng.cn/"));
        List<User> users = new ArrayList<>();
        User user = new User();
        user.setId(1l);
        user.setAge(11);
        user.setName("zhangsan");
        users.add(user);
        user = new User();
        user.setId(2l);
        user.setAge(22);
        user.setName("lisi");
        users.add(user);
        model.addAttribute("users", users);
        return thymeleaf("/index");
    }

}