package com.bishe.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bishe.pojo.FileInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * 文件数据访问对象
 * @date 2022/02/17 54:01:15
 */
@Mapper
public interface FileMapper extends BaseMapper<FileInfo> {

}

