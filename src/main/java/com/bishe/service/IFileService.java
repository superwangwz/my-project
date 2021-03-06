package com.bishe.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bishe.pojo.FileInfo;
import com.bishe.pojo.query.FileQuery;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


/**
 * 文件接口
 * @date 2022/02/17 54:01:15
 */
public interface IFileService extends IService<FileInfo> {

    List<FileInfo> getByDataId(String dataId);

    FileInfo upload(MultipartFile file, String dataId,String type);

    void deleteFile(String fileId);

    IPage<FileInfo> toPage(FileQuery query);
}
