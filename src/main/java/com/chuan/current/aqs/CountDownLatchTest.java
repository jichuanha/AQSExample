package com.chuan.current.aqs;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * author:jc
 * Date:2019/2/11
 * Time:13:35
 * CountDownLatch同步工具类,他容许一个或多个线程等待,直到其他线程结束再执行
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
                    //减少锁存器的计数,如果当前计数大于零,则它将递减.如果计数达到零，释放所有等待的线程
                    downLatch.countDown();
                }
            });
        }
        //如果当前计数大于零，则当前线程将被禁用以进行线程调度，并处于休眠状态
        //等到锁存器计数为零时,才能继续进行下去
        downLatch.await();
        executorService.shutdown();
        System.out.println("finish" + TOTAL_MONEY);

    }

    private static void getCount() {
        TOTAL_MONEY = TOTAL_MONEY +1;
    }


    public static void test(int threadnum) throws InterruptedException {
        Thread.sleep(1000);// 模拟请求的耗时操作
        System.out.println("threadnum:" + threadnum);
        Thread.sleep(1000);// 模拟请求的耗时操作
    }

}
