package com.example.demo.volatiles;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author xsx
 *
 * volatile是java虚拟机轻量级同步机制 1保证可见性 2不保证原子性 3禁止指令重排
 *
 *  1.验证volatile的可见性
 *      int number = 0; 不添加volatile关键字，没有可见性。
 *      volatile int number = 0; 添加volatile关键字，其他线程可见，保证可见性
 *  2.验证volatile不保证原子性
 *      2.1 原子性指什么意思
 *          不可分割，完整性。要么同时成功，要么同时失败
 *      2.2  vilatile不保证原子性的案例演示
 *          线程争抢，线程通过工作内存写回到JMM主内存时，造成重复写入。值丢失
 *
 *      2.3 原子性解决
 *          1.添加sync字段
 *          2.使用juc下的atomicInteger类。底层通过CAS原理实现
 *  3.禁止指令重排
 *      为了提供性能，编译器和处理器会对指令做重排，分为三种情况：
 *      源代码 》编译器优化的重排 》指令并行的重排 》内存系统的重排 》最终执行的指令
 *   1.单线程环境里面确保程序最终执行结果和代码顺序执行的结果一致
 *   2.处理器在进行重排序时必须要考虑指令之间的数据依赖性
 *   3.多线程环境中线程交替执行，由于编译器优化重排的存在，俩个线程中使用的变量能否保证一致性是无法确定的。结果无法预测
 *
 *
 */
public class VolatileDemo {

    public static void main(String[] args) {
//        seeOkByVolatile();

        MyData myData = new MyData();

        for (int i = 0; i < 20; i++) {
            new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    myData.addPlusPlus();
                    myData.addAtomic();
                }
            }, String.valueOf(i)).start();
         }

        //如果线程数大于2，表示上面线程没执行完成。
        while (Thread.activeCount() > 2) {
            Thread.yield();
        }

        System.out.println(Thread.currentThread().getName() + "\t finally number value：" + myData.number);
        System.out.println(Thread.currentThread().getName() + "\t finally number value：" + myData.atomicInteger);
    }

    /**
     * volatile可以保证可见性，及时通知其它线程，主物理内存的值已经被修改
     */
    private static void seeOkByVolatile() {
        MyData myData = new MyData();

        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "\t come in");
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            myData.add();
            System.out.println(Thread.currentThread().getName() + "\t update: " + myData.number);
        }, "AAA").start();

        // 第二个线程是main线程
        while (myData.number == 0) {
            //main线程一直等待循环，直到number值不在等于0
        }

        System.out.println(Thread.currentThread().getName() + "\t mission is over");
    }

}


class MyData {

    volatile int number = 0;

    public void add() {
        this.number = 60;
    }

    public void addPlusPlus() {
        number++;
    }

    AtomicInteger atomicInteger = new AtomicInteger();
    public void addAtomic() {
        atomicInteger.getAndIncrement();
    }
}
