package com.util.learn.file.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.file.Files;

/**
 * @author acer
 */
public class FileNativeUtil {

    private static final Logger logger = LoggerFactory.getLogger(FileNativeUtil.class);

    public static void main(String[] args) throws IOException {

    }
    public static void test1(){
        File file = new File("C:\\Users\\acer\\Desktop\\测试\\新建文本文档.txt");
        BufferedWriter bufferedWriter = getBufferedWriter(file);
        writeOneLineToFile(bufferedWriter,"你好123");
        writeOneLineToFile(bufferedWriter,"你好123456");
        closeBufferedWriter(bufferedWriter);
    }

    /**
     * 打开txt文件
     * openTxt("C:\\Users\\acer\\Desktop\\测试\\","新建文本文档.txt");
     * @param parentFolder
     * @param filename
     */
    public static void openTxt(String parentFolder, String filename) {
        try {
            Desktop.getDesktop().open(new File(parentFolder, filename));
        } catch (IOException var3) {
            logger.error(var3.getMessage());
        }

    }

    /**
     * 效率高
     *  copyFileUsingFileChannels(new File("C:\\Users\\acer\\Desktop\\测试\\新建文本文档.txt")
     *  ,new File("C:\\Users\\acer\\Desktop\\测试\\新建文本文档xxxx.txt"));
     * 复制文件
     * @param source
     * @param dest
     * @throws IOException
     */
    private static void copyFileUsingFileChannels(File source, File dest) throws IOException {
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            inputChannel = new FileInputStream(source).getChannel();
            outputChannel = new FileOutputStream(dest).getChannel();
            outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
        } finally {
            inputChannel.close();
            outputChannel.close();
        }
    }
     private static void copyFileUsingJava7Files(File source, File dest)
             throws IOException {
             Files.copy(source.toPath(), dest.toPath());
     }
    /**
     * 创建文件
     * @param filePath
     * @return
     */
    public static File createFile(String filePath){
        try {
            File file = new File(filePath);
            if (file.exists() && file.isFile()) {
                file.delete();
                file.createNewFile();
            }
            return file;
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return null;
    }

    /**
     * 创建文件夹
     * @param filePath
     * @return
     */
    public static File createDirectory(String filePath){
        try {
            File dir=new File(filePath);
            if(!dir.exists()) {
                dir.mkdirs();
            }
            return dir;
        }catch (Exception e){
            logger.error("创建目录失败！{}",e.getMessage());
        }
        return null;
    }

    /**
     * 操作文件写入一行
     * @param bw
     * @param msg
     */
    public static void writeOneLineToFile(BufferedWriter bw, String msg) {
        try {
            bw.write(msg);
            bw.newLine();
            bw.flush();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * 获取文件缓存写入字符
     * @param file
     * @return
     */
    public static BufferedWriter getBufferedWriter(File file){
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return bw;
    }

    /**
     * 关闭缓存写入字符
     * @param bw
     */
    public static void closeBufferedWriter(BufferedWriter bw) {
        try {
            if (bw != null) {
                bw.close();
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * 获取缓存字符读取
     * @param file
     * @return
     */
    public static BufferedReader getBufferedReader(File file){
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return br;
    }

    /**
     * 关闭缓存读取字符流
     * @param br
     */
    public static void closeBufferedReader(BufferedReader br) {
        try {
            if (br != null) {
                br.close();
            }
        }catch (Exception e){
            logger.error(e.getMessage());
        }
    }

    /**
     * 获取文件字节流
     * @param file
     * @return
     */
    public static BufferedInputStream getBufferedInputStream(File file){
        BufferedInputStream bis = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(file));
        } catch (Exception e){
            logger.error(e.getMessage());
        }
        return bis;
    }

    /**
     * 关闭文件字节流
     * @param bis
     */
    public static void closeBufferedInputStream(BufferedInputStream bis){
        try {
            if (bis != null) {
                bis.close();
            }
        }catch (Exception e){
            logger.error(e.getMessage());
        }
    }



}
