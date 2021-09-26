package com.util.learn.file.utils;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件处理工具类
 * @author liuye
 */
public class FileUtils
{
    public static String FILENAME_PATTERN = "[a-zA-Z0-9_\\-\\|\\.\\u4e00-\\u9fa5]+";

    /**
     *
     * @param targetPath
     * @param file
     * @return
     * @throws IOException
     */
    public static String uploadFile(String targetPath,MultipartFile file,String filename) throws IOException {
        if(!file.isEmpty()){
            //构建存储路径
            String path= targetPath;
            //构建父路径
            File filePath=new File(path);
            if(!filePath.exists()){
                filePath.mkdirs();
            }
            File tempFile=new File(path+File.separator+filename);
            file.transferTo(tempFile);
            return filename;
        }
        return null;
    }
    /**
     * 下载文件
     * @param request
     * @param response
     * @param file
     */
    public static void downloadFile(HttpServletRequest request, HttpServletResponse response, File file, boolean deleteOnExit){
        response.setCharacterEncoding(request.getCharacterEncoding());
        response.setContentType("application/octet-stream");
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            String originFileName = file.getName();
            response.setHeader("Content-Disposition", "attachment; filename="+FileUtils.setFileDownloadHeader(request, originFileName));
            IOUtils.copy(fis,response.getOutputStream());
            response.flushBuffer();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                    if(deleteOnExit){
                        file.deleteOnExit();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    /**
     * 输出指定文件的byte数组
     * 
     * @param filePath 文件路径
     * @param os 输出流
     * @return
     */
    public static void writeBytes(String filePath, OutputStream os) throws IOException
    {
        FileInputStream fis = null;
        try
        {
            File file = new File(filePath);
            if (!file.exists())
            {
                throw new FileNotFoundException(filePath);
            }
            fis = new FileInputStream(file);
            byte[] b = new byte[1024];
            int length;
            while ((length = fis.read(b)) > 0)
            {
                os.write(b, 0, length);
            }
        }
        catch (IOException e)
        {
            throw e;
        }
        finally
        {
            if (os != null)
            {
                try
                {
                    os.close();
                }
                catch (IOException e1)
                {
                    e1.printStackTrace();
                }
            }
            if (fis != null)
            {
                try
                {
                    fis.close();
                }
                catch (IOException e1)
                {
                    e1.printStackTrace();
                }
            }
        }
    }
    /**
     * 获取指定目录下的文件列表
     *
     * @param targetPath 文件
     * @return
     */
    public static List<String> getTargetPathFiles(String targetPath){
        List<String> filePathList=new ArrayList<>();
        File file = new File(targetPath);
        if (file.exists()) {
            if (file.isDirectory()) {
                File files[] = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    filePathList.add(files[i].getName());
                }
            }
        }
        return filePathList;
    }

    public static void main(String[] args) {

        System.out.println(deleteFileAll("C:\\Users\\acer\\Desktop\\测试\\页面跳转"));

    }
    /**
     * 删除文件
     * 
     * @param filePath 文件
     * @return 成功删除返回true
     */
    public static boolean deleteFile(String filePath)
    {
        boolean flag = false;
        File file = new File(filePath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists())
        {
            file.delete();
            flag = true;
        }
        return flag;
    }
    /**
     * 删除指定目录下所有文件
     *
     * @param filePath 文件夹路径
     * @return 成功删除返回true
     */
    public static boolean deleteFileAll(String filePath) {
        boolean flag = false;
        File file = new File(filePath);
        if (file.exists()) {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    files[i].delete();
                }
                flag = true;
            }
        }
        return flag;
    }


    /**
     * 获取扩展名
     * @param originalFilename
     * @return
     */
    public static String getFileExtension(String originalFilename) {
        String originalFileName=originalFilename.toLowerCase();
        return originalFileName.substring(originalFileName.lastIndexOf("."));
    }
    /**
     * 文件名称验证
     * 
     * @param filename 文件名称
     * @return true 正常 false 非法
     */
    public static boolean isValidFilename(String filename)
    {
        return filename.matches(FILENAME_PATTERN);
    }

    /**
     * 下载文件名重新编码
     * 
     * @param request 请求对象
     * @param fileName 文件名
     * @return 编码后的文件名
     */
    public static String setFileDownloadHeader(HttpServletRequest request, String fileName)
            throws UnsupportedEncodingException
    {
        final String agent = request.getHeader("USER-AGENT");
        String filename = fileName;
        if (agent.contains("MSIE"))
        {
            // IE浏览器
            filename = URLEncoder.encode(filename, "utf-8");
            filename = filename.replace("+", " ");
        }
        else if (agent.contains("Firefox"))
        {
            // 火狐浏览器
            filename = new String(fileName.getBytes(), "ISO8859-1");
        }
        else if (agent.contains("Chrome"))
        {
            // google浏览器
            filename = URLEncoder.encode(filename, "utf-8");
        }
        else
        {
            // 其它浏览器
            filename = URLEncoder.encode(filename, "utf-8");
        }
        return filename;
    }
}
