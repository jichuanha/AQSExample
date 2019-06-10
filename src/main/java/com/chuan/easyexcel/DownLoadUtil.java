package com.chuan.easyexcel;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jc
 * @Description:
 * @date 2019/6/10
 */
public class DownLoadUtil {

    public static void main(String[] args) {
        OutputStream out = null;
        try {
            out = new FileOutputStream("C:\\Users\\sj\\Desktop\\333.xlsx");
            ExcelWriter writer = new ExcelWriter(out, ExcelTypeEnum.XLSX);
            Sheet sheet1 = new Sheet(1, 0,User.class);
            List<User> datas = new ArrayList<>();
            User user = new User();
            user.setAge(18);
            user.setName("张三");
            user.setHeight(173);
            user.setWeight(73);
            User user1 = new User();
            user1.setAge(19);
            user1.setName("李四");
            user1.setHeight(172);
            user1.setWeight(72);
            User user2 = new User();
            user2.setAge(16);
            user2.setName("王二");
            user2.setHeight(171);
            user2.setWeight(71);
            datas.add(user);
            datas.add(user1);
            datas.add(user2);

            writer.write(datas, sheet1);
            writer.finish();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}
