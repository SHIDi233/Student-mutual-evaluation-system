package com.example.web_test.utils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.metadata.CellExtra;
import com.alibaba.excel.read.listener.PageReadListener;
import com.alibaba.excel.read.listener.ReadListener;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExcelUtils {
    public static List<ExcelData> readExcel(InputStream file) {
        List<ExcelData> res = new ArrayList<>();
        EasyExcel.read(file, ExcelData.class, new PageReadListener<ExcelData>(res::addAll)).sheet().doReadSync();
        return res;
    }
}
