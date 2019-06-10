package com.chuan.easyexcel;

import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.metadata.Sheet;


import java.io.*;

/**
 * @author jc
 * @Description:
 * @date 2019/5/24
 */
public class ReadUtil {

    public static void main(String[] args) {
        InputStream im = null;
        try {
            im = new FileInputStream(new File("C:\\Users\\sj\\Desktop\\22.xlsx"));
            ExcelListener excelListener = new ExcelListener();
            ExcelReader reader = new ExcelReader(im,  null, excelListener );
            reader.read(new Sheet(1, 1, User.class));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            try {
                im.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
