package com.bishe.util;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import com.bishe.config.EcoBootException;
import com.bishe.pojo.FileInfo;
import com.bishe.service.IFileService;
import lombok.SneakyThrows;
import org.springframework.context.ApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.UUID;

/**
 * 项目工具类
 * @date 2022/2/16 15:55
 */
public class ProjectUtils {

    private static ApplicationContext applicationContext;

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static void setApplicationContext(ApplicationContext applicationContext) {
        ProjectUtils.applicationContext = applicationContext;
    }

    @Resource
    private IFileService fileService;


    /**
     * 检查登录
     */
    public static void checkLogin(){
        boolean login = StpUtil.isLogin();

        if (login){
            return;
        }
        throw new EcoBootException("未登录！");
    }
    /**
     * 获取登录id
     * @return
     */
    public static  String getLoginId(){
        boolean login = StpUtil.isLogin();
        if (login){
            return (String) StpUtil.getLoginId();
        }
        return "";
    }
    /**
     * 获取存储文件路径
     * @return
     */
    public static String getDirPath(){
        File file1 = new File(".file");

        //返回绝对路径
        return file1.getAbsolutePath();
    }

    /**
     * 下载文件
     * @param response
     */
    public static  void download(HttpServletResponse response, FileInputStream fileInputStream,String name,String type){
        try {
            byte[] bytes = IoUtil.readBytes(fileInputStream);
            String s = new String(bytes);
            //加密类型
            switch (type){
                case "zuc":bytes = ZUCUtil.encryption(s).getBytes();break;
                case "sm2":bytes = Sm2Util.encryptionOrDecryption(bytes,false);break;
                case "sm4": bytes = Sm4Util.decryptFile(bytes);break;
                default:throw new EcoBootException("没有此类型！");
            }
            //获取文件真实名字
            String fileName = name.split("_")[1];
            //设置传输文件类型 和文件名
            response.setHeader("content-disposition","attachment;filename="+ URLEncoder.encode(fileName,"utf-8"));
            //获取response 输入流
            ServletOutputStream outputStream = response.getOutputStream();
            //写入数据
            outputStream.write(bytes);
            //关闭流
            outputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                fileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 上传文件
     * @param file
     * @return
     */
    @SneakyThrows
    public static String uploadFile(MultipartFile file,String type){
        //获取文件名
        String name = file.getOriginalFilename();
        //将所有下划线替换成- 以防影响真实名切割
        name = name.replace("_", "-");
        //获取文件大小
        long size = file.getSize();
        //转换成M(兆)
        double m = (double)size/1024/1024;
        //文件后缀
        String suffix = FileUtil.getSuffix(name);
        //是图片
        if ("png".equalsIgnoreCase(suffix)||"jpg".equalsIgnoreCase(suffix)){
            //超过10M 不能上传
            if (m > 10){
                return "";
            }
        }
        //超过200M不能上传
        if (m > 200){
            return "";
        }
        //设置一个不重复的文件名
        String fileName =  UUID.randomUUID().toString()+"_" + name;
        //获取输出流
        InputStream inputStream = file.getInputStream();
        byte[] bytes = IoUtil.readBytes(inputStream);
        //获取存放文件的目录路径
        String filePath = getDirPath();
        //生成一个不重名的地址
        String path = filePath +File.separator+fileName;
        System.out.println("path = " + path);
        //获取输入流
        FileOutputStream fileOutputStream= new FileOutputStream(new File(path));

        //字符串文件内容
        String fileContent = new String(bytes);
        //加密类型
        switch (type){
            case "zuc":bytes = ZUCUtil.encrypt(fileContent).getBytes();break;
            case "sm2":bytes = Sm2Util.encryptionOrDecryption(bytes,true);break;
            case "sm4": bytes = Sm4Util.encryptFile(bytes);break;
            default:throw new EcoBootException("没有此类型！");
        }
        //写
        fileOutputStream.write(bytes);
        //刷新
        fileOutputStream.flush();
        fileOutputStream.close();
        inputStream.close();

        return fileName;
    }

    /**
     * 获取文件
     * @return
     */
    public static File getFile(FileInfo fileInfo){

        // 文件信息为空直接返回
        if (fileInfo == null){
            throw new EcoBootException("没有该文件！");
        }

        //获取文件名
        String name = fileInfo.getName();
        //获取存储文件的目录
        String dirPath = ProjectUtils.getDirPath();
        //文件的下载路径
        String filePath = dirPath +File.separator + name;

        return new File(filePath);
    }

}
