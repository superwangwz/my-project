package com.bishe.util;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.io.Serializable;

/**
 * 分页工具
 * @date 2022/3/8 14:11
 */
@Data
public class PageOrder implements Serializable {
    //当前页
    private Integer pageNo;
    //每页有的数据量
    private Integer pageSize;

    public <T> Page<T> toPage(){
        //默认值
        if (pageNo == null || pageSize == null){
            pageNo = 1;
            pageSize = 5;
        }

        return new Page<>(pageNo, pageSize);
    }
}
