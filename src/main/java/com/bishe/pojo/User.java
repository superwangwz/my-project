package com.bishe.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@ApiModel("文件")
@TableName("user")
@EqualsAndHashCode(callSuper = false)
public class User implements Serializable {

    @TableId(type = IdType.INPUT)
    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("真实姓名")
    private String realName;

    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelProperty("用户名")
    private String passWord;

    @ApiModelProperty("用户名")
    @TableField(exist = false)
    private String token;
}
