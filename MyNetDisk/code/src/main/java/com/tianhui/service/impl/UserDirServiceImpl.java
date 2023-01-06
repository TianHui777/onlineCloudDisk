package com.tianhui.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tianhui.entity.File;
import com.tianhui.entity.UserDir;
import com.tianhui.Dao.UserDirDao;
import com.tianhui.service.FileService;
import com.tianhui.service.UserDirService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户目录树业务逻辑
 */
@Service
public class UserDirServiceImpl extends ServiceImpl<UserDirDao, UserDir> implements UserDirService {
    @Autowired
    private FileService service;

    @Override
    public UserDir getUserDir(String id) {
        UserDir userDir = baseMapper.selectById(id);
        return userDir;
    }

    @Override
    public int setUserDir(UserDir userDir) {
        return baseMapper.updateById(userDir);
    }

    @Override
    public boolean deleteStruct(String userId, String url) {
        QueryWrapper<File> wrapper = new QueryWrapper<>();
        wrapper.like("f_dir", url);
        wrapper.eq("mem_id", userId);
        boolean b = service.remove(wrapper);
        return b;
    }

}
