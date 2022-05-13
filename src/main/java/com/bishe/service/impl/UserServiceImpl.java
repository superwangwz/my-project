package com.bishe.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bishe.dao.UserMapper;
import com.bishe.pojo.User;
import com.bishe.service.IUserService;
import org.springframework.stereotype.Service;

/**
 * 文件接口实现
 * @date 2022/02/17 54:01:15
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {


        @Override
        public User login(User user) {
            String userName = user.getUserName();
            String password = user.getPassWord();

            //根据用户名密码查找
            User one = getOne(new LambdaQueryWrapper<User>()
                            .eq(User::getUserName, userName)
                            .eq(User::getPassWord, password)
                    ,false);

            if (one == null) {
                return null;
            }
            StpUtil.login(one.getId());
            return one;
    }
}
