package com.chuan;

/**
 * author:jc
 * Date:2019/2/13
 * Time:9:53
 * 单例双重校验锁实例
 */
public class Ttest {

    private volatile static Ttest ttest;

    public static Ttest getTest() {
        //先判断是否为空,如果不为空,就不用进入锁,提升性能
        if(ttest == null) {
            synchronized (Ttest.class){
                //再次判断是否为空,是为了防止多个线程同时进入,当有个一线程进入锁后,另外一个线程也已经进入第一个非空判断了.
                if(ttest == null) {
                    ttest = new Ttest();
                }
            }
        }
        return ttest;
    }

    public void get() {
        System.out.println("吃饭!");
    }

}

class eat{
    public static void main(String[] args) {
        Ttest test = Ttest.getTest();
        test.get();
    }

}
