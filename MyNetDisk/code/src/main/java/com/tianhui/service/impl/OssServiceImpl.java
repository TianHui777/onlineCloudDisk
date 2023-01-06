package com.tianhui.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tianhui.entity.File;
import com.tianhui.service.FileService;
import com.tianhui.service.OssService;
import com.tianhui.service.UserService;
import com.tianhui.utils.ConstanPropertiesUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 阿里云OSS业务逻辑
 */
@Service
public class OssServiceImpl implements OssService {
    //
    @Autowired
    private OSS ossClient;// 注入阿里云oss文件服务器客户端
//    @Autowired
//    private AliyunOssConfig aliyunOssConfig;// 注入阿里云OSS基本配置类

    @Autowired
    private FileService fileService;

    @Autowired
    private UserService userService;

    //上传头像到oss
    @Override
    public com.tianhui.entity.File upload(MultipartFile file, String catalogue) {
        // 工具类获取值
        String endpoint = ConstanPropertiesUtils.END_POIND;
        String accessKeyId = ConstanPropertiesUtils.ACCESS_KEY_ID;
        String accessKeySecret = ConstanPropertiesUtils.ACCESS_KEY_SECRET;
        String bucketName = ConstanPropertiesUtils.BUCKET_NAME;

        try {
            // 创建OSS实例。
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
            File file1 = new File();
            //获取上传文件输入流
            InputStream inputStream = file.getInputStream();
            //获取文件名称
            String originalFilename = file.getOriginalFilename();
            //获取文件类型
            String fileType = originalFilename.substring(originalFilename.lastIndexOf("."));
            String name = originalFilename.substring(0, originalFilename.indexOf("."));
            String type = fileType.substring(1);
            if (type.equals("bmp") || type.equals("mp3") || type.equals("jpg") || type.equals("jpeg") || type.equals("png") || type.equals("mp3") || type.equals("tif") || type.equals("gif")
                    || type.equals("pcx") || type.equals("tga") || type.equals("exif") || type.equals("fpx") || type.equals("svg") || type.equals("psd") || type.equals("cdr")
                    || type.equals("pcd") || type.equals("dxf") || type.equals("ufo") || type.equals("eps") || type.equals("ai") || type.equals("raw")
                    || type.equals("WMF") || type.equals("webp") || type.equals("mp3") || type.equals("avif")) {
                file1.setFiletype("image");
            }

            //2 把文件按照日期进行分类
            //获取当前日期
            String datePath = new DateTime().toString("yyyy/MM/dd");
            //拼接
            originalFilename = datePath + "/" + originalFilename;

            //调用oss方法实现上传
            //第一个参数  Bucket名称
            //第二个参数  上传到oss文件路径和文件名称
            //第三个参数  上传文件输入流
            ossClient.putObject(bucketName, originalFilename, inputStream);

            // 关闭OSSClient。
            ossClient.shutdown();

            //把上传之后文件路径返回
            //需要把上传到阿里云oss路径手动拼接出来
            String url = "https://" + bucketName + "." + endpoint + "/" + originalFilename;
            file1.setName(name);
            file1.setType(type);
            file1.setUrl(url);
            file1.setFDir(catalogue);
            file1.setSize(file.getSize());
            return file1;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
     * 文件删除
     */
    public String delete(String id) {
        String endpoint = ConstanPropertiesUtils.END_POIND;
        String accessKeyId = ConstanPropertiesUtils.ACCESS_KEY_ID;
        String accessKeySecret = ConstanPropertiesUtils.ACCESS_KEY_SECRET;
        String bucketName = ConstanPropertiesUtils.BUCKET_NAME;
        // 日期目录
        QueryWrapper<File> wrapper = new QueryWrapper<>();
        wrapper.eq("id", id);
        File fileServiceOne = fileService.getOne(wrapper);
        String name = fileServiceOne.getName();
        boolean remove = fileService.remove(wrapper);
        if (remove == true) {
            System.out.println("删除成功");
        } else {
            System.out.println("删除失败");
        }
        SimpleDateFormat data = new SimpleDateFormat("yyyy/MM/dd");
        Date gmtCreate = fileServiceOne.getGmtCreate();
        data.format(gmtCreate);
        //String filePath = new DateTime().toString("yyyy/MM/dd");
        String filePath = data.toString();
        //System.out.println(filePath);
        try {
            OSSClient ossClient = new OSSClient(endpoint,
                    accessKeyId, accessKeySecret);
            // 根据BucketName,filetName删除文件
            // 删除目录中的文件
            String fileKey = filePath + "/" + name;
            ossClient.deleteObject(bucketName, fileKey);
            try {
            } finally {
                ossClient.shutdown();
            }
            System.out.println("文件删除！");
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    @Override
    public String uploadfile(MultipartFile file) {
        try {
            //accessKeyId, accessKeySecret
            //fileName：上传文件原始名称
            String fileName = file.getOriginalFilename();
            //title：上传之后显示名称
            String title = fileName.substring(0, fileName.lastIndexOf("."));
            //inputStream：上传文件输入流
            InputStream inputStream = file.getInputStream();
            UploadStreamRequest request = new UploadStreamRequest(ConstanPropertiesUtils.ACCESS_KEY_ID, ConstanPropertiesUtils.ACCESS_KEY_SECRET, title, fileName, inputStream);

            UploadVideoImpl uploader = new UploadVideoImpl();
            UploadStreamResponse response = uploader.uploadStream(request);

            String videoId = null;
            if (response.isSuccess()) {
                videoId = response.getVideoId();
                System.out.println(response.getRequestId());
            } else {
                videoId = response.getVideoId();
            }
            return videoId;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public String deleteVideo(String id) {
        QueryWrapper<File> wrapper = new QueryWrapper<>();
        wrapper.eq("id", id);
        File file = fileService.getOne(wrapper);
        boolean b = fileService.remove(wrapper);
        System.out.println(file);
        return file.getVideoId();
    }

    @Override
    public String uploadFileAvatar(MultipartFile file) {
        // 工具类获取值
        String endpoint = ConstanPropertiesUtils.END_POIND;
        String accessKeyId = ConstanPropertiesUtils.ACCESS_KEY_ID;
        String accessKeySecret = ConstanPropertiesUtils.ACCESS_KEY_SECRET;
        String bucketName = ConstanPropertiesUtils.BUCKET_NAME;

        try {
            // 创建OSS实例。
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

            //获取上传文件输入流
            InputStream inputStream = file.getInputStream();
            //获取文件名称
            String fileName = file.getOriginalFilename();


            //2 把文件按照日期进行分类
            //获取当前日期
            String datePath = new DateTime().toString("yyyy/MM/dd");
            //拼接
            fileName = datePath + "/" + fileName;

            //调用oss方法实现上传
            //第一个参数  Bucket名称
            //第二个参数  上传到oss文件路径和文件名称   aa/bb/1.jpg
            //第三个参数  上传文件输入流
            ossClient.putObject(bucketName, fileName, inputStream);

            // 关闭OSSClient。
            ossClient.shutdown();

            //把上传之后文件路径返回
            //需要把上传到阿里云oss路径手动拼接出来
            String url = "https://" + bucketName + "." + endpoint + "/" + fileName;
            return url;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
