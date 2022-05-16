package com.bishe.pojo.query;

import com.bishe.util.PageOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class FileQuery extends PageOrder {

    @ApiModelProperty("文件名")
    private String name;
}
