package com.util.learn.http.utils;

import cn.hutool.core.io.FileUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.util.Iterator;
import java.util.Map;

/**
 * header-token（key+Timespan+SecretKey 组成的 32 位 md5 加密的大写字符串）
 * restTemplate接口请求工具
 * 调用接口：http://xxx/api/radar/download/getFile?key=公司Key&url=这块填写下载链接
 * @author acer
 */
public class RestTemplateUtil {

    /**
     * 日志
     */
    private static final Logger log = LoggerFactory.getLogger(RestTemplateUtil.class);

    /**
     * 设置请求头
     */
    private static HttpHeaders headers = new HttpHeaders();

    public static void main(String[] args) throws IOException {
        downloadFile(HttpMethod.GET,"https://img-blog.csdnimg.cn/20210921132029691.png?x-oss-process=image/watermark,type_ZHJvaWRzYW5zZmFsbGJhY2s,shadow_50,text_Q1NETiBA54Ot5rC05YW76bKo6bG8,size_20,color_FFFFFF,t_70,g_se,x_16","C:\\Users\\acer\\Desktop\\测试\\testio\\x.zip");
    }

    /**
     * post发送json数据
     * @param url
     * @param json
     * @return
     */
    public String postJson(String url,String json){
        RestTemplate restTemplate = new RestTemplate();

        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());

        HttpEntity<String> formEntity = new HttpEntity<String>(json, headers);
        String result = null;
        result = restTemplate.postForEntity(url, formEntity, String.class).getBody();

        return result;
    }
    /**
     * Get请求下载
     * @param url
     * @param pathname
     * @throws IOException
     */
    public static void downloadFileByGet(String url,String pathname) throws IOException {
        downloadFile(HttpMethod.GET,url,pathname);
    }
    /**
     * 下载文件
     * @param url 下载地址
     * @param pathname 存储路径
     * @throws IOException
     */
    public static void downloadFile(HttpMethod method,String url,String pathname) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            log.info("开始下载url:{}",url);
            ResponseEntity<byte[]> response = restTemplate.exchange(
                    url,
                    method,
                    new HttpEntity<byte[]>(headers),
                    byte[].class);
            byte[] result = response.getBody();
            inputStream = new ByteArrayInputStream(result);
            File file = new File(pathname);
            if (!file.exists())
            {
                FileUtil.touch(file);
            }
            outputStream = new FileOutputStream(file);
            int len = 0;
            byte[] buf = new byte[1024*5];
            while ((len = inputStream.read(buf, 0, 1024*5)) != -1) {
                outputStream.write(buf, 0, len);
            }
            outputStream.flush();
            log.info("下载结束");
        }finally {
            if(inputStream != null){
                inputStream.close();
            }
            if(outputStream != null){
                outputStream.close();
            }
        }

    }

    public static void setHeaders(Map<String,String> headersMap){
        //设置请求头
        HttpHeaders headers = new HttpHeaders();
        Iterator<Map.Entry<String, String>> entries = headersMap.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<String, String> entry = entries.next();
            headers.set(entry.getKey(), entry.getValue());
        }

    }

    private static String[] RandomAuthentHeader(String appkey,String seckey) {
        String timeSpan = String.valueOf(System.currentTimeMillis() / 1000);
        String[] authentHeaders = new String[] { DigestUtils.md5Hex(appkey.concat(timeSpan).concat(seckey)).toUpperCase(), timeSpan };
        return authentHeaders;
    }
}
