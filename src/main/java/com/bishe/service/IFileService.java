package com.bishe.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bishe.pojo.FileInfo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


/**
 * 文件接口
 * @date 2022/02/17 54:01:15
 */
public interface IFileService extends IService<FileInfo> {

    List<FileInfo> getByDataId(String dataId);

    void upload(MultipartFile file, String dataId,String type);

    void deleteFile(String fileId);
}
