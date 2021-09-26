package com.util.learn.shell.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * @author ly
 */
@Slf4j
public class ShellTaskCallUtil {


    /**
     * sh /usr/my/xx.sh param1 param2 ...
     * 执行sh脚本文件
     * @param runCmd sh脚本文件地址
     * @param paramsValue 脚本参数集合
     */
    public static void exec(String runCmd, List<String> paramsValue){
        if(runCmd==null){
            log.error("run-cmd指令为空");
            return;
        }
        log.info("执行run-cmd");
        StringBuffer paramStrBuffer = new StringBuffer();
        if(paramsValue.size()>0){
            for(String paramValue:paramsValue){
                paramStrBuffer.append(" ").append(paramValue);
            }
        }
        Process ps = null;
        BufferedReader br=null;
        try {
            // 多个参数可以在param1后面继续增加，但不要忘记空格！！
            String cmd = "sh "+ runCmd + paramStrBuffer.toString();
            log.info("cmd:{}",cmd);
            ps = Runtime.getRuntime().exec(cmd);
            int exitValue = ps.waitFor();
            log.info("call shell exitValue:{}",exitValue);
            if (0 != exitValue) {
                log.error("call shell failed. error code is :" + exitValue);
            }
            //读取sh运行时输出的内容
            br = new BufferedReader(new InputStreamReader(ps.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            log.info(sb.toString());
            log.info("run-cmd执行结束");
        }
        catch (Exception e) {
            log.error("run-cmd异常:{}",e);
        }finally {
            try {
                if (br!=null){
                    br.close();
                }
            } catch (IOException e) {
                log.error("run-cmd-IOException异常:{}",e);
            }
        }
        if (ps != null) {
            ps.destroy();
        }
    }

}
