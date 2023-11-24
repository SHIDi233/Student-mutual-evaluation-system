package com.example.web_test.utils;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class ExcelData {
    @ExcelProperty(value = "学校")
    private String name;

    @ExcelProperty(value = "学号")
    private String ID;

}

