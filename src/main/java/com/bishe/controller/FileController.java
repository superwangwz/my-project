package com.bishe.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bishe.config.EcoBootException;
import com.bishe.pojo.FileInfo;
import com.bishe.pojo.query.FileQuery;
import com.bishe.service.IFileService;
import com.bishe.util.ProjectUtils;
import com.bishe.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * 文件控制器
 * @date 2022/2/16 16:59
 */
@RestController
@CrossOrigin
@RequestMapping("/file")
@Api(tags = "文件控制器")
public class FileController {

    @Resource
    private IFileService fileService;


    @ApiOperation("上传文件")
    @PostMapping("/upload")
    public Result upload(MultipartFile file,String dataId,String type){
        FileInfo fileInfo = fileService.upload(file, dataId, type);
        return Result.ok("上传成功!",fileInfo);
    }


    @ApiOperation("下载文件")
    @GetMapping("/download/{fileId}")
    public Result download(@PathVariable String fileId, HttpServletResponse response){
        FileInfo fileInfo = fileService.getById(fileId);
        if (fileInfo == null){
             throw new EcoBootException("文件为空！");
        }
        File file = ProjectUtils.getFile(fileInfo);
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            //下载文件
            ProjectUtils.download(response,fileInputStream,file.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Result.ok();
    }


    @ApiOperation("删除文件")
    @DeleteMapping("/deleteFile/{fileId}")
    public Result deleteFile(@PathVariable String fileId){
       fileService.deleteFile(fileId);
        return Result.ok();
    }


    @ApiOperation("查询")
    @GetMapping("/toPage")
    public Result list(FileQuery query){
        //检查登录
        IPage<FileInfo> iPage = fileService.toPage(query);
        return Result.ok(iPage);
    }

}
