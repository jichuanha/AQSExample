package com.aqs;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * author:jc
 * Date:2019/2/11
 * Time:11:55
 * 信号量,可以指定特定个数线程同时访问某个资源.
 */
public class SemaphoreTest {

    // 请求的数量
    private static final int threadCount = 550;

    public static void main(String[] args) throws InterruptedException {
        // 创建一个具有固定线程数量的线程池对象（如果这里线程池的线程数量给太少的话你会发现执行的很慢）
        ExecutorService threadPool = Executors.newFixedThreadPool(300);
        // 一次只能允许执行的线程数量。
        final Semaphore semaphore = new Semaphore(20, true);

        for (int i = 0; i < threadCount; i++) {
            final int threadnum = i;
            // Lambda 表达式的运用
            threadPool.execute(() -> {
                try {
                    semaphore.acquire();// 获取一个许可，所以可运行线程数量为20/1=20
                    test(threadnum);
                    System.out.println("=========================" );
                    semaphore.release();// 释放一个许可,如果不释放,线程将阻塞.
                    System.out.println("+++++++++++++++++++++++++++" );
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            });
        }
        threadPool.shutdown();
        System.out.println("finish");
    }

    public static void test(int threadnum) throws InterruptedException {
        Thread.sleep(1000);// 模拟请求的耗时操作
        System.out.println("threadnum:" + threadnum);
        Thread.sleep(1000);// 模拟请求的耗时操作
    }
}
