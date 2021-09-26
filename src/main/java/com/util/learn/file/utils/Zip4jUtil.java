package com.util.learn.file.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.AesKeyStrength;
import net.lingala.zip4j.model.enums.CompressionMethod;
import net.lingala.zip4j.model.enums.EncryptionMethod;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static net.lingala.zip4j.util.InternalZipConstants.CHARSET_UTF_8;

/**
 * 解压缩工具类
 * @author ly
 */
@Slf4j
public class Zip4jUtil {


    /**
     * 压缩文件集合
     * @param filesToAdd 文件集合
     * @param path 压缩包存储路径
     * @param filename 压缩包命名
     * @throws ZipException
     */
    public static void zip(List<File> filesToAdd,String path,String filename) throws ZipException {
        ZipParameters zipParameters = new ZipParameters();
        ZipFile zipFile = new ZipFile(path+filename);
        zipFile.addFiles(filesToAdd, zipParameters);
    }
    /**
     * 解压缩指定文件并存入指定路径
     * @param targetPath 待解压文件
     * @param destinationPath 解压文件存放位置
     * @param password 解压密码
     * @param isDelete 解压后是否删除
     */
    public static boolean unZip(String  targetPath,String destinationPath,String password,boolean isDelete) throws ZipException {

        File file =new File(targetPath);

        ZipFile zipFile =new ZipFile(file);
        //设置编码
        zipFile.setCharset(CHARSET_UTF_8);
        //是否有密码
        if (!StrUtil.isEmpty(password)) {
            zipFile.setPassword(password.toCharArray());
        }
        log.debug("zip解压路径：{}",destinationPath);
        File destinationPathFile= new File(destinationPath);
        if(!destinationPathFile.exists()){
            //不存在该路径，初始化路径
            FileUtil.mkdir(destinationPathFile);
        }
        //解压
        zipFile.extractAll(destinationPath);

        //获取包含文件
        List<FileHeader> fileHeaders = zipFile.getFileHeaders();
        fileHeaders.stream().forEach(fileHeader -> log.debug(fileHeader.getFileName()));
        //内含文件数较少-一般为3
        //生成.ok文件
        for(FileHeader fileHeader:fileHeaders){
            File insideFile =new File(fileHeader.getFileName());
            String mainName = FileNameUtil.mainName(insideFile);
            FileUtil.touch(destinationPath+File.separator+mainName+".ok");
        }

        if(isDelete==true){
            boolean del = FileUtil.del(file);
            log.info("解压完毕，删除压缩包-is-success:{}",del);
        }
        return true;

    }

    /**
     * 弃用
     * 解压缩指定文件并存入指定路径
     * @param targetPath 待解压文件
     * @param destinationPath 解压文件存放位置
     * @param password 解压密码
     * @param isDelete 解压后是否删除
     */
    public static boolean unZipForSeparator(String  targetPath,String destinationPath,String password,boolean isDelete) throws ZipException {

        File file =new File(targetPath);

        ZipFile zipFile =new ZipFile(file);
        //设置编码
        zipFile.setCharset(CHARSET_UTF_8);
        //是否有密码
        if (!StrUtil.isEmpty(password)) {
            zipFile.setPassword(password.toCharArray());
        }
        log.debug("zip解压路径：{}",destinationPath);
        File destinationPathFile= new File(destinationPath);
        if(!destinationPathFile.exists()){
            //不存在该路径，初始化路径
            FileUtil.mkdir(destinationPathFile);
        }
        //解压

        //获取包含文件
        List<FileHeader> fileHeaders = zipFile.getFileHeaders();
        fileHeaders.stream().forEach(fileHeader -> log.debug(fileHeader.getFileName()));
        //内含文件数较少-一般为3
        for(FileHeader fileHeader:fileHeaders){
            File insideFile =new File(fileHeader.getFileName());
            String mainName = FileNameUtil.mainName(insideFile);
            zipFile
                    .extractFile(fileHeader.getFileName(), destinationPath+File.separator+mainName);
            FileUtil.touch(destinationPath+File.separator+mainName+File.separator+mainName+".ok");
        }

        //zipFile.extractAll(destinationPath);

        if(isDelete==true){
            boolean del = FileUtil.del(file);
            log.info("解压完毕，删除压缩包-is-success:{}",del);
        }
        return true;

    }

    public static void zip() throws ZipException {
        ZipParameters zipParameters = new ZipParameters();
        zipParameters.setEncryptFiles(true);


        zipParameters.setCompressionMethod(CompressionMethod.DEFLATE);
        zipParameters.setEncryptionMethod(EncryptionMethod.AES);
        zipParameters.setAesKeyStrength(AesKeyStrength.KEY_STRENGTH_256);

        List<File> filesToAdd = Arrays.asList(
                new File("C:\\Users\\acer\\Desktop\\测试\\zip测试\\你好hello.txt")
        );

        ZipFile zipFile = new ZipFile
                ("C:\\Users\\acer\\Desktop\\测试\\zip测试\\123\\"+"filename.zip"
                        , "123".toCharArray());
        zipFile.addFiles(filesToAdd, zipParameters);

    }

}
