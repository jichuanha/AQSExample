package com.aqs;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * author:jc
 * Date:2019/2/11
 * Time:11:55
 * �ź���,����ָ���ض������߳�ͬʱ����ĳ����Դ.
 */
public class SemaphoreTest {

    // ���������
    private static final int threadCount = 550;

    public static void main(String[] args) throws InterruptedException {
        // ����һ�����й̶��߳��������̳߳ض�����������̳߳ص��߳�������̫�ٵĻ���ᷢ��ִ�еĺ�����
        ExecutorService threadPool = Executors.newFixedThreadPool(300);
        // һ��ֻ������ִ�е��߳�������
        final Semaphore semaphore = new Semaphore(20, true);

        for (int i = 0; i < threadCount; i++) {
            final int threadnum = i;
            // Lambda ���ʽ������
            threadPool.execute(() -> {
                try {
                    semaphore.acquire();// ��ȡһ����ɣ����Կ������߳�����Ϊ20/1=20
                    test(threadnum);
                    System.out.println("=========================" );
                    semaphore.release();// �ͷ�һ�����,������ͷ�,�߳̽�����.
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
        Thread.sleep(1000);// ģ������ĺ�ʱ����
        System.out.println("threadnum:" + threadnum);
        Thread.sleep(1000);// ģ������ĺ�ʱ����
    }
}
