package com.chuan.easyexcel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;

import java.io.Serializable;

/**
 * @author jc
 * @Description:
 * @date 2019/5/24
 */
@Data
public class User extends BaseRowModel implements Serializable {
    private static final long serialVersionUID = -5292012382958574865L;

    @ExcelProperty(index = 0, value = "姓名", format = "utf-8")
    private String name;
    @ExcelProperty(index = 1, value = "年龄", format = "utf-8")
    private Integer age;
    @ExcelProperty(index = 3, value = "体重", format = "utf-8")
    private Integer weight;
    @ExcelProperty(index = 2, value = "身高", format = "utf-8")
    private Integer height;


}
