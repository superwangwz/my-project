package com.bishe.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bishe.pojo.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户数据访问层
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
