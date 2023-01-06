package com.tianhui.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.tianhui.entity.UserDir;

public interface UserDirService extends IService<UserDir> {

    UserDir getUserDir(String id);

    int setUserDir(UserDir userDir);

    boolean deleteStruct(String userId, String url);
}
