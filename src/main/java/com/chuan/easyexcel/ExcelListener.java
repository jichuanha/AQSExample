package com.chuan.easyexcel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jc
 * @Description:解析监听器，
 *  每解析一行会回调invoke()方法。
 *  整个excel解析结束会执行doAfterAllAnalysed()方法
 * @date 2019/5/24
 */
public class ExcelListener extends AnalysisEventListener {

    private List<Object> data = new ArrayList<>();

    @Override
    public void invoke(Object o, AnalysisContext analysisContext) {

        System.out.println(("当前行 : "+analysisContext.getCurrentRowNum()));
        System.out.println("当前行值 : "+ o);

        data.add(o);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        //
    }
}
