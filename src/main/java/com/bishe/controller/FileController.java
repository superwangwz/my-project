package com.bishe.controller;

import com.bishe.pojo.FileInfo;
import com.bishe.service.IFileService;
import com.bishe.util.ProjectUtils;
import com.bishe.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

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
        fileService.upload(file,dataId,type);
        return Result.ok("上传成功!");
    }


    @ApiOperation("下载文件")
    @PostMapping("/download")
    public Result download(String fileId, HttpServletResponse response,String type){
        File file = ProjectUtils.getFile(fileId);
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            //下载文件
            ProjectUtils.download(response,fileInputStream,file.getName(),type);
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
    @GetMapping("/list")
    public Result list(){
        //检查登录
        List<FileInfo> list = fileService.list();
        return Result.ok(list);
    }

}
