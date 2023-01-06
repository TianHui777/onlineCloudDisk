package com.tianhui.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tianhui.entity.User;
import com.tianhui.vo.RegisterVo;
import com.tianhui.service.UserService;
import com.tianhui.utils.R;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户相关接口
 */
@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    //登录
    @ApiOperation(value = "登录")
    @PostMapping("login")
    public R loginUser(@RequestBody User user) {
        String token = userService.login(user);
        User user1 = userService.login1(user);
        return R.ok().data("token", token).data("mem", user1);
    }

    //注册
    @PostMapping("register")
    public R registerUser(@RequestBody RegisterVo registerVo) {
        userService.register(registerVo);
        return R.ok();
    }

    //查询用户信息
    @ApiOperation(value = "根据用户表id查询用户信息")
    @GetMapping("/{id}")
    public R getMemberInfo(@PathVariable String id) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("id", id);
        User user = userService.getOne(wrapper);
        return R.ok().data("member", user);
    }

    //修改用户信息
    @ApiOperation(value = "更新用户信息")
    @PostMapping("updateMemberInfo")
    public R updateMemberInfo(@RequestBody User user) {
        String id = user.getId();
        QueryWrapper<User> w = new QueryWrapper<>();
        w.eq("id", id);
        User one = userService.getOne(w);
        User member = new User();
        member.setId(user.getId());
        member.setNeicun(one.getNeicun());
        member.setAvatar(user.getAvatar());
        member.setNickname(user.getNickname());
        boolean b = userService.updateById(member);
        if (b) {
            return R.ok();
        } else {
            return R.error();
        }
    }
}

