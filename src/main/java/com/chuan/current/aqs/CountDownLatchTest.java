package com.chuan.current.aqs;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * author:jc
 * Date:2019/2/11
 * Time:13:35
 * CountDownLatchͬ��������,������һ�������̵߳ȴ�,ֱ�������߳̽�����ִ��
 */
public class CountDownLatchTest {

    private static final int THREAD_COUNT = 500;

    private static volatile int TOTAL_MONEY ;

    public static void main(String[] args) throws Exception{
        ExecutorService executorService = Executors.newFixedThreadPool(300);
        final CountDownLatch downLatch = new CountDownLatch(THREAD_COUNT);
        for (int a = 0; a < THREAD_COUNT; a ++) {
            final int threadNum = a;
            executorService.execute(() -> {
                try {
                    getCount();
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    //�����������ļ���,�����ǰ����������,�������ݼ�.��������ﵽ�㣬�ͷ����еȴ����߳�
                    downLatch.countDown();
                }
            });
        }
        //�����ǰ���������㣬��ǰ�߳̽��������Խ����̵߳��ȣ�����������״̬
        //�ȵ�����������Ϊ��ʱ,���ܼ���������ȥ
        downLatch.await();
        executorService.shutdown();
        System.out.println("finish" + TOTAL_MONEY);

    }

    private static void getCount() {
        TOTAL_MONEY = TOTAL_MONEY +1;
    }


    public static void test(int threadnum) throws InterruptedException {
        Thread.sleep(1000);// ģ������ĺ�ʱ����
        System.out.println("threadnum:" + threadnum);
        Thread.sleep(1000);// ģ������ĺ�ʱ����
    }

}
