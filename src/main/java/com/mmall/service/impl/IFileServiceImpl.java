package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.mmall.service.IFileService;
import com.mmall.util.FTPUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;


/**
 * Created by 张凡 on 2019/10/15.
 */
@Service("iFileService")
public class IFileServiceImpl implements IFileService {
    private Logger logger = (Logger) LoggerFactory.getLogger(IFileServiceImpl.class);
    public String upload(MultipartFile file ,String path) {

        String fileName = file.getOriginalFilename();
        /*
        1.file.getOriginalFilename()是得到上传时的文件名
        2.获取文件扩展名
         */
        String fileExtensionName = fileName.substring(fileName.lastIndexOf(".")+1);
        String uploadFileName = UUID.randomUUID().toString()+"."+fileExtensionName;

        logger.info("开始上传文件，上传的文件名:{},上传的路径:{},新文件名:{}",fileName,path,uploadFileName);

        /*
        创建一个文件夹
         */
        File fileDir = new File(path);
        if(!fileDir.exists()) {
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }

        /*
        文件放在路径下面，这里包括文件名(包含扩展名)和路径名
         */
        File targetFile = new File(path,uploadFileName);

        try {
            //文件上传到对应目录
            file.transferTo(targetFile);
            //上传到ftp服务器
            FTPUtil.uploadFile(Lists.newArrayList(targetFile));
            //删除Tomcat目录中对应的文件
            targetFile.delete();
        } catch (IOException e) {
            logger.error("文件上传异常",e);
            return null;
        }
        return targetFile.getName();
    }
}
