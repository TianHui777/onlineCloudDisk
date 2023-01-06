package com.tianhui.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tianhui.entity.File;
import com.tianhui.Dao.FileDao;
import com.tianhui.service.FileService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 文件管理业务逻辑
 */
@Service
public class FileServiceImpl extends ServiceImpl<FileDao, File> implements FileService {
    @Override
    public List<File> getAllFileInfo(String userId) {
        QueryWrapper<File> wrapper = new QueryWrapper<>();
        wrapper.eq("mem_id", userId);
        //File files = baseMapper.selectById(wrapper);
        List<File> fileList = baseMapper.selectList(wrapper);
        return fileList;
    }

    @Override
    public List<File> getFileInfo(String id) {
        QueryWrapper<File> wrapper = new QueryWrapper<>();
        wrapper.eq("id", id);
        //File files = baseMapper.selectById(wrapper);
        List<File> files = baseMapper.selectList(wrapper);
        //System.out.println(files);
        return files;
    }

    /**
     * 获取当前目录下的所有文件
     */
    @Override
    public List<File> getCurFiles(String dir, String id) {
        QueryWrapper<File> wrapper = new QueryWrapper<>();
        wrapper.eq("f_dir", dir);
        wrapper.eq("mem_id", id);
        List<File> files = baseMapper.selectList(wrapper);
        return files;
    }

    @Override
    public File getFiles(String id) {
        QueryWrapper<File> wrapper = new QueryWrapper<>();
        wrapper.eq("id", id);
        File file = baseMapper.selectOne(wrapper);
        return file;
    }

    @Override
    public List<File> getFindFile(String userId, String name) {
        QueryWrapper<File> wrapper = new QueryWrapper<>();
        wrapper.eq("mem_id", userId);
        wrapper.like("name", name);
        List<File> fileList = baseMapper.selectList(wrapper);
        return fileList;
    }

    @Override
    public List<File> getList(String userId, String url, int result, String name) {
        QueryWrapper<File> wrapper = new QueryWrapper<>();
        wrapper.eq("mem_id", userId);
        wrapper.like("f_dir", url);
        List<File> fileList = baseMapper.selectList(wrapper);
        for (int i = 0; i < fileList.size(); i++) {
            String fDir = fileList.get(i).getFDir();
            String[] s = fDir.split("/", -1);
            s[result] = name;
            StringBuffer sb = new StringBuffer();
            //sb.append("/");
            for (int j = 1; j < s.length; j++) {
                sb.append("/").append(s[j]);
            }
            System.out.println(sb.toString());
            fileList.get(i).setFDir(sb.toString());
        }

        System.out.println(fileList);
        return fileList;
    }


}
