package com.aqs;

/**
 * author:jc
 * Date:2019/2/13
 * Time:9:53
 * ����˫��У����ʵ��
 */
public class Ttest {

    private volatile static Ttest ttest;

    public static Ttest getTest() {
        if(ttest == null) {
            synchronized (Ttest.class){
                if(ttest == null) {
                    ttest = new Ttest();
                }
            }
        }
        return ttest;
    }

    public void get() {
        System.out.println("�Է�!");
    }

}

class eat{
    public static void main(String[] args) {
        Ttest test = Ttest.getTest();
        test.get();
    }

}
