package com.bishe.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bishe.pojo.User;


/**
 * 文件接口
 * @date 2022/02/17 54:01:15
 */
public interface IUserService extends IService<User> {


    /**
     * 用户登录
     * @return
     */
    User login(User user);
}
