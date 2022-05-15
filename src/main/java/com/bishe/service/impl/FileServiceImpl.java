package com.bishe.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bishe.config.EcoBootException;
import com.bishe.dao.FileMapper;
import com.bishe.pojo.FileInfo;
import com.bishe.pojo.query.FileQuery;
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
    public void upload(MultipartFile file, String dataId,String type) {
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

        String fileName = ProjectUtils.uploadFile(file,type);

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
        fileInfo1.setName(fileName.substring(fileName.indexOf("_")+1,fileName.lastIndexOf(".")));
        fileInfo1.setType(type);
        save(fileInfo1);
    }

    @Override
    public void deleteFile(String id) {
            FileInfo fileInfo = this.getById(id);
            if (fileInfo == null){
                return;
            }
            String fileName = fileInfo.getName();

            String dirPath = ProjectUtils.getDirPath();
            //文件的下载路径
            String filePath = dirPath +File.separator + fileName;

            File file = new File(filePath);

            FileUtil.del(file);

            this.remove(new LambdaQueryWrapper<FileInfo>().eq(FileInfo::getId,id));
    }

    @Override
    public IPage<FileInfo> toPage(FileQuery query) {
        LambdaQueryWrapper<FileInfo> wrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(query.getName())){
            wrapper.like(FileInfo::getName,query.getName());
        }
        wrapper.orderByDesc(FileInfo::getCreateTime);

        return page(query.toPage(), wrapper);
    }
}
