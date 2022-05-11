package com.bishe.pojo;

import lombok.Data;
import java.io.Serializable;

/**
 登录模型
 * @date 2022/2/15 15:27
 */
@Data
public class LoginModel implements Serializable {


    /**
     * 用户名
     */
    private String userName;
    /**
     * 密码
     */
    private String password;

    /**
     * 验证码
     */
    private String txt;

    /**
     * 记住我
     */
    private Boolean rememberMe;
}
