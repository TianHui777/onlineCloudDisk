package com.tianhui.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tianhui.entity.User;
import com.tianhui.vo.RegisterVo;
public interface UserService extends IService<User> {

    //登录
    String login(User user);

    User login1(User user);
    // 注册
    void register(RegisterVo registerVo);
}
