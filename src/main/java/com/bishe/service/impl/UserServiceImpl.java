package com.bishe.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bishe.config.EcoBootException;
import com.bishe.dao.UserMapper;
import com.bishe.pojo.User;
import com.bishe.service.IUserService;
import com.bishe.util.ToHash;
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
            //加密为hash
            String hash = ToHash.changeHash(password);

            //根据用户名密码查找
            User one = getOne(new LambdaQueryWrapper<User>()
                            .eq(User::getUserName, userName)
                            .eq(User::getPassWord, hash)
                    ,false);

            if (one == null) {
                return null;
            }
            StpUtil.login(one.getId());
            return one;
    }

    @Override
    public void addOrUpdate(User user) {
        if (StrUtil.isNotEmpty(user.getId())){
            updateById(user);
            return;
        }
        User one = getOne(new LambdaQueryWrapper<User>().eq(User::getUserName, user.getUserName()),false);
        if (one!= null){
            throw new EcoBootException("用户名已存在！");
        }
        String passWord = user.getPassWord();
        //加密为hash
        String hash = ToHash.changeHash(passWord);
        user.setPassWord(hash);
        save(user);
    }
}
