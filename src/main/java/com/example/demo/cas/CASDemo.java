package com.example.demo.cas;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author xsx
 *
 * 连带流程： CAS ---> Unsafe ---> CAS底层思想 ---> ABA ---> 原子引用更新 ---> 如何规避ABA问题
 *
 * CAS的全称为Compare-And-Swap,它是一条cup并发原语。 功能是判断内存某个位置的值是否为期望值，如果是则改为新值，这个过程是原子性。
 * CAS并发原语体现在java语言中就是sun.misc.Unsafe类中的各个方法，调用Unsafe类中的CAS方法，JVM会帮我们实现出CAS汇编指令，这是一种完全依赖
 * 硬件的功能，通过他实现了原子操作。原语的执行必须是连续的，在执行过程中不允许被中断，也就是说CAS是一条CPU的原子指令，不会造成数据不一致的问题。
 *
 * 流程：比较当前工作内存中和主内存中的值，如果相同则执行规定操作。否则继续比较当前主内存和当前内存的值一致为止。
 * CAS应用：CAS有三个操作数，内存值V，旧的预期值A，更新值B。
 *         当且仅当预期值A和内存值V相同时，将内存值V修改为B。
 *
 * 1.cas是什么？ ===> compareAndSet
 *   比较并交换
 *
 *   CAS缺点：
 *      1.如果一直失败，循环时间长。CPU开销大
 *      2.只能保证一个共享变量的原子操作
 *      3.ABA问题
 *          原因：俩个线程同时获取主内存值V到各地本地内存中，A线程10秒执行一次。B线程2秒执行一次。B线程首先把V改为B，又把B改回了V。而A线程开始执行，发现值不变，执行成功。
 */
public class CASDemo {

    public static void main(String[] args) {

        /**
         * 主物理内存（堆） 如果期望值和内存值一致，则修改内存值。如果期望值和内存值不一致，则false，修改失败
         */
        AtomicInteger atomicInteger = new AtomicInteger(5);
        System.out.println(atomicInteger.compareAndSet(5, 2020) + "\t current data: "+ atomicInteger.get());
        System.out.println(atomicInteger.compareAndSet(5, 1024) + "\t current data: "+ atomicInteger.get());

        atomicInteger.getAndIncrement();

//        public final int getAndIncrement() {
//            return unsafe.getAndAddInt(this, valueOffset, 1); this:当前对象  valueOffset：内存偏移量
//        }
        /**
         * 1.Unsafe类
         *  是CAS的核心类，由于java方法无法直接访问底层系统，需要通过本地（native）方法来访问，Unsafe相当于一个后门，基于该类可以直接操作特定内存的数据。
         *  Unsafe类存在于sun.misc包中，其内部方法操作可以像c的指针一样直接操作内存。因为java中CAS操作的执行依赖与Unsafe类的方法。
         *  注意：Unsafe类中的所有方法都是native修饰，也就是说Unsafe类中的方法直接调用操作系统底层资源执行相应任务
         */

        /**
         * CAS执行过程：
         * 1.AtomicInteger里面的value原始值为5，既主内存中的AtomicInteger的原始值为5，根据jmm模型，线程A和线程B各执行一个值为5的value副本到各自的工作内存。
         * 2.线程A通过this.getIntVolatile(var1, var2);拿到value值为5.这时线程A被挂起。
         * 3.线程B也通过this.getIntVolatile(var1, var2);拿到value值为5，线程B并执行compareAndSwapInt方法。成功修改内存值为6.
         * 4.这时线程A恢复，执行compareAndSwapInt方法比较，发现手中的值和主内存中的值不一致，重新执行步骤2.
         * 5.重新获取value值，因为变量value被Volatile修饰，所有线程共享变量。线程A继续执行compareAndSwapInt方法比较。直到成功。
         */
    }
}
