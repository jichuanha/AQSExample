package com.aqs;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * author:jc
 * Date:2019/2/14
 * Time:9:55
 */
public class CASTest {

    /**
     * ͨ��ʵ�鷢��,
     * �����addCount��ʹ��synchronized�ؼ���,���Ա�֤ԭ����,ʱ���Լ130����,
     * ���ʹ��AtomicҲ���Ա�֤ԭ����,ʱ���ԼΪ70����,��Լ���Խ�ʡһ���ʱ��.
     * Atomic�ڲ�����:
     *      �Ὣ����Ҫ���ֵ���ڴ��д��ֵ���бȽ�,�����ͬ,�Ż�����ֵ���в���,��ɺ��ٴ����ڴ���.
     * ����ʹ��Atomic����һ����������:
     *      1.��ʱ,�������ֵ�Ƚϲ���ͬ,CAS�ͻ�һֱ����,���Ի��cpu���ӿ���
     *      2.ֻ�ܱ�֤һ��ֵ��ԭ����.
     *      3.�ᷢ��ABA����.����ڴ�ֵA���޸�ΪB,Ȼ���ֱ��޸�ΪA,�����������޸�,�������������A,��ʱCAS�����ֲ�����.
     */


    /**
     * volateֻ�ܱ�֤�ɼ���,���ܱ�֤ԭ����
     */
    private static volatile int count = 0;

    /**
     * Atomic �ܱ�֤ԭ����
     */
    private static AtomicInteger atomicInteger = new AtomicInteger(0);
    public static  void addCount() {
        //count ++;
        atomicInteger.getAndIncrement();
        atomicInteger.addAndGet(20);
    }

    public static void main(String[] args) throws Exception{
        long start = System.currentTimeMillis();
        ExecutorService executorService = Executors.newFixedThreadPool(20);
        CountDownLatch countDownLatch = new CountDownLatch(20);

        for (int a = 0; a< 20; a++) {
            executorService.execute(() -> {
                try {
                    for (int b = 0; b < 100000; b ++) {
                        addCount();
                    }
                } finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();
        executorService.shutdown();
        long end = System.currentTimeMillis();
        System.out.println(end-start);
        System.out.println(" count : " + atomicInteger);
    }


}
