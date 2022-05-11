package com.bishe.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bishe.config.EcoBootException;
import com.bishe.dao.FileMapper;
import com.bishe.pojo.FileInfo;
import com.bishe.service.IFileService;
import com.bishe.util.ProjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.UUID;

/**
 * 文件接口实现
 * @date 2022/02/17 54:01:15
 */
@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, FileInfo> implements IFileService {

    @Override
    public List<FileInfo> getByDataId(String dataId) {

        return list(new LambdaQueryWrapper<FileInfo>()
                .eq(FileInfo::getDataId, dataId)
        );

    }

    @Override
    public void upload(MultipartFile file, String dataId) {
        if (StrUtil.isEmpty(dataId)){
            throw new EcoBootException("dataId为空！");
        }

        //检查登录
        ProjectUtils.checkLogin();

        if (file==null){
            throw new EcoBootException("文件为空！");
        }

        //如果有文件先删除
        List<FileInfo> byDataId = getByDataId(dataId);
        if (CollUtil.isNotEmpty(byDataId)){
            byDataId.forEach(s ->{
                deleteFile(s.getId());
            });
        }

        String fileName = ProjectUtils.uploadFile(file);

        if (StrUtil.isEmpty(fileName)){
            throw new EcoBootException("格式错误！");
    }
        //上传成功则保存文件记录
        FileInfo fileInfo1 = new FileInfo();
        fileInfo1.setId(UUID.randomUUID().toString());
        fileInfo1.setDataId(dataId);
        fileInfo1.setCreateBy(ProjectUtils.getLoginId());
        fileInfo1.setCreateTime(new DateTime());
        fileInfo1.setSuffix(FileUtil.getSuffix(fileName));
        fileInfo1.setName(FileUtil.getName(fileName));
        save(fileInfo1);
    }

    @Override
    public void deleteFile(String fileId) {
        File file = ProjectUtils.getFile(fileId);
        //删除文件
        FileUtil.del(file);
        removeById(fileId);
    }
}
