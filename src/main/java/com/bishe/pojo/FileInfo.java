package com.bishe.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 文件
 * @date 2022/02/17 54:01:15
 */
@Data
@ApiModel("文件")
@TableName("file")
@EqualsAndHashCode(callSuper = false)
public class FileInfo implements Serializable {

    @TableId(type = IdType.INPUT)
    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("业务id")
    private String dataId;

    @ApiModelProperty("加密类型")
    private String type;

    @ApiModelProperty("文件名")
    private String name;

    @ApiModelProperty("文件后缀")
    private String suffix;

    @ApiModelProperty("创建人")
    private String createBy;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("创建时间")
    private Date createTime;

}
