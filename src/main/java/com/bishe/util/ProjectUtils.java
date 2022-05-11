package com.bishe.util;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;

import com.bishe.config.EcoBootException;
import com.bishe.pojo.FileInfo;
import com.bishe.pojo.LoginModel;
import com.bishe.service.IFileService;
import lombok.SneakyThrows;
import org.springframework.context.ApplicationContext;
import org.springframework.web.multipart.MultipartFile;

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

    /**
     * 生成验证码
     * @param response
     */
    public static void createImgCode(HttpServletResponse response){
        SaSession tokenSession = StpUtil.getTokenSession();

        //定义图形验证码的长和宽
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(200, 100);

        //图形验证码写出，可以写出到文件，也可以写出到流
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            lineCaptcha.write(outputStream);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        tokenSession.set("imgCode",lineCaptcha);
        //设置一个创建时间
        tokenSession.setCreateTime(new DateTime().getTime());
    }

    /**
     * 获取验证码code
     * @return
     */
    public static String getImgCode(){
        //获取当前Token Session
        SaSession tokenSession = StpUtil.getTokenSession();
        //获取验证码类
        LineCaptcha imgCode = (LineCaptcha) tokenSession.get("imgCode");

        //获取验证码创建时间
        long createTime = tokenSession.getCreateTime();
        //当前时间
        long now = new DateTime().getTime();
        //时间大于1分钟
        if (now-createTime > 5*60*1000||imgCode == null){
           throw new EcoBootException("验证码过期！");
        }

        //返回验证码code
        return imgCode.getCode();
    }
    /**
     * 登录效验
     */
    public static void checkLoginModel(LoginModel loginModel){

        if(loginModel ==null){
            throw new EcoBootException("登录失败!");
        }

        //验证 验证码
        checkImgCode(loginModel.getTxt());

        if (StrUtil.isEmpty(loginModel.getUserName())||StrUtil.isEmpty(loginModel.getPassword())){
            throw new EcoBootException("用户名密码为空!");
        }
    }

    /**
     * 登录效验
     */
    public static void checkImgCode(String txt){
        //获取验证码
        String code = ProjectUtils.getImgCode();

        if (!code.equalsIgnoreCase(txt)){
            throw new EcoBootException("验证码错误!");
        }
    }

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
        File file1 = new File("files");

        //返回绝对路径
        return file1.getAbsolutePath();
    }

    /**
     * 下载文件
     * @param response
     * @param file
     */
    public static  void download(HttpServletResponse response, File file){
        try {
            //获取文件流
            FileInputStream fileInputStream = new FileInputStream(file);
            //文件大小
            int available = fileInputStream.available();
            //初始化缓存
            byte[] bytes = new byte[available];
            //写入缓存中
            fileInputStream.read(bytes);
            //获取文件名
            String name = file.getName();
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
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 上传文件
     * @param file
     * @return
     */
    @SneakyThrows
    public static String uploadFile(MultipartFile file){
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
        int available = inputStream.available();
        //缓存
        byte[] bytes = new byte[available];
        //读
        inputStream.read(bytes);
        //获取存放文件的目录路径
        String filePath = getDirPath();
        //生成一个不重名的地址
        String path = filePath +File.separator+fileName;
        System.out.println("path = " + path);
        //获取输入流
        FileOutputStream fileOutputStream= new FileOutputStream(new File(path));
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
     * @param fileId
     * @return
     */
    public static File getFile(String fileId){
        IFileService fileService = getApplicationContext().getBean(IFileService.class);

        // 文件id为空
        if (fileId == null){
            throw new EcoBootException("fileId 为空！");
        }
        //根据id查找文件信息
        FileInfo byId = fileService.getById(fileId);
        // 文件信息为空直接返回
        if (byId == null){
            throw new EcoBootException("没有该文件！");
        }

        //获取文件名
        String name = byId.getName();
        //获取存储文件的目录
        String dirPath = ProjectUtils.getDirPath();
        //文件的下载路径
        String filePath = dirPath +File.separator + name;

        return new File(filePath);
    }

}