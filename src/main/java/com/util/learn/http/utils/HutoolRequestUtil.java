package com.util.learn.http.utils;


import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

/**
 * 请求下载接口 工具类
 * @author ly
 */
@Slf4j
public class HutoolRequestUtil {


    public static  void  downloadFile(String fileUrl,String saveFilePath){
            File saveFile = new File(saveFilePath);

            if(!saveFile.exists()){
                FileUtil.touch(saveFile);
            }

            log.info("开始下载url:{}",fileUrl);
            //带进度显示的文件下载
            HttpUtil.downloadFile(fileUrl, FileUtil.file(saveFile));
            log.info("下载结束");

    }


}
