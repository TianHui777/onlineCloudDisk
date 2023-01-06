package com.tianhui.service;

import com.tianhui.entity.File;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

public interface OssService {
    File upload(MultipartFile file, String catalogue);

    String delete(String id);

    String uploadfile(MultipartFile file);

    String deleteVideo(String id);

    String uploadFileAvatar(MultipartFile file);
}
