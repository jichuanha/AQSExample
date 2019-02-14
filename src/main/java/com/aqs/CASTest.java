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
     * 通过实验发现,
     * 如果在addCount上使用synchronized关键字,可以保证原子性,时间大约130毫秒,
     * 如果使用Atomic也可以保证原子性,时间大约为70毫秒,大约可以节省一半的时间.
     * Atomic内部规则:
     *      会将现在要变得值和内存中存的值进行比较,如果相同,才会对这个值进行操作,完成后再存入内存中.
     * 所以使用Atomic会有一下三个问题:
     *      1.耗时,如果两个值比较不相同,CAS就会一直尝试,所以会给cpu增加开销
     *      2.只能保证一个值的原子性.
     *      3.会发生ABA问题.如果内存值A被修改为B,然后又被修改为A,经过了两次修改,但是最后结果还是A,这时CAS就区分不出来.
     */


    /**
     * volate只能保证可见性,不能保证原子性
     */
    private static volatile int count = 0;

    /**
     * Atomic 能保证原子性
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
