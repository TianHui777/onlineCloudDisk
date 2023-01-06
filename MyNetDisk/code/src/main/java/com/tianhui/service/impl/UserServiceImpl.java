package com.tianhui.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.tianhui.entity.User;
import com.tianhui.vo.RegisterVo;
import com.tianhui.handler.SpaceException;
import com.tianhui.Dao.UserDao;
import com.tianhui.service.UserService;
import com.tianhui.utils.JwtUtils;
import com.tianhui.utils.MD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 用户相关业务逻辑
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements UserService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    //登录的方法
    public String login(User user) {
        //获取登录手机号和密码
        String mobile = user.getMobile();
        String password = user.getPassword();
        System.out.println(mobile + password);
        //手机号和密码非空判断
        if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password)) {
            throw new SpaceException(20001, "登录失败");
        }
        //判断手机号是否正确
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile", mobile);
        User mobileMember = baseMapper.selectOne(wrapper);
        //判断查询对象是否为空
        if (mobileMember == null) {//没有这个手机号
            throw new SpaceException(20001, "登录失败");
        }

        //判断密码
        //因为存储到数据库密码肯定加密的
        //把输入的密码进行加密，再和数据库密码进行比较
        //加密方式 MD5
        if (!MD5.encrypt(password).equals(mobileMember.getPassword())) {
            throw new SpaceException(20001, "登录失败");
        }
        //登录成功
        //生成token字符串，使用jwt工具类
        String jwtToken = JwtUtils.getJwtToken(mobileMember.getId(), mobileMember.getNickname());
        QueryWrapper<User> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("mobile", user.getMobile());
        baseMapper.selectOne(wrapper1);
        return jwtToken;
    }

    //登录的方法
    public User login1(User user) {
        //获取登录手机号和密码
        String mobile = user.getMobile();
        String password = user.getPassword();
        System.out.println(mobile + password);
        //手机号和密码非空判断
        if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password)) {
            throw new SpaceException(20001, "手机号或密码为空，登录失败");
        }
        //判断手机号是否正确
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile", mobile);
        User mobileMember = baseMapper.selectOne(wrapper);
        //判断查询对象是否为空
        if (mobileMember == null) {//没有这个手机号
            throw new SpaceException(20001, "未注册，登录失败");
        }

        //判断密码
        //因为存储到数据库密码肯定加密的
        //把输入的密码进行加密，再和数据库密码进行比较
        //加密方式 MD5
        if (!MD5.encrypt(password).equals(mobileMember.getPassword())) {
            throw new SpaceException(20001, "登录失败");
        }
        //登录成功
        //生成token字符串，使用jwt工具类
        //String jwtToken = JwtUtils.getJwtToken(mobileMember.getId(), mobileMember.getNickname());
        QueryWrapper<User> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("mobile", user.getMobile());
        User user1 = baseMapper.selectOne(wrapper1);
        return user1;
    }

    //注册
    public void register(RegisterVo registerVo) {
        //获取注册的数据
//        String code = registerVo.getCode(); //验证码
        // 默认111验证码
        String code = "111"; //验证码
        String mobile = registerVo.getMobile(); //手机号
        String nickname = registerVo.getNickname(); //昵称
        String password = registerVo.getPassword(); //密码
        String avatar = registerVo.getAvatar();//头像
        //非空判断
        if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password)
                || StringUtils.isEmpty(code) || StringUtils.isEmpty(nickname)
        ) {
            throw new SpaceException(20001, "不能为空");
        }
        //判断手机号是否重复，表里面存在相同手机号不进行添加
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile", mobile);
        Integer count = baseMapper.selectCount(wrapper);
        if (count > 0) {
            throw new SpaceException(20001, "手机号已存在，注册失败！");
        }
        //数据添加数据库中
        User user = new User();
        user.setMobile(mobile);
        user.setNickname(nickname);
        user.setAvatar(avatar);
        user.setPassword(MD5.encrypt(password));
        baseMapper.insert(user);
    }


}
