package com.chuan;

/**
 * author:jc
 * Date:2019/2/13
 * Time:9:53
 * ����˫��У����ʵ��
 */
public class Ttest {

    private volatile static Ttest ttest;

    public static Ttest getTest() {
        //���ж��Ƿ�Ϊ��,�����Ϊ��,�Ͳ��ý�����,��������
        if(ttest == null) {
            synchronized (Ttest.class){
                //�ٴ��ж��Ƿ�Ϊ��,��Ϊ�˷�ֹ����߳�ͬʱ����,���и�һ�߳̽�������,����һ���߳�Ҳ�Ѿ������һ���ǿ��ж���.
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
