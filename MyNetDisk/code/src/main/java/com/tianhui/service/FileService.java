package com.tianhui.service;

import com.tianhui.entity.File;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface FileService extends IService<File> {

    List<File> getAllFileInfo(String memId);

    List<File> getFileInfo(String id);

    List<File> getCurFiles(String userDir,String id);

    File getFiles(String id);

    List<File> getFindFile(String userId,String name);

    List<File> getList(String userId, String url,int result,String name);
}
