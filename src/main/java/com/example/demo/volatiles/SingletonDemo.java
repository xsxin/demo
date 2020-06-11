package com.example.demo.volatiles;

/**
 * @author xsx
 *
 * 多线程使用单例模式不安全。解决方式：
 *  1.方法添加synchronize关键字
 *  2.使用DCL（Double check lock双端捡锁机制）
 *      不一定线程安全，多线程下存在指令重排的存在。加入volatile可以禁止指令重排。读取到的singletonDemo不为null时，singletonDemo的引用对象可能没有完成初始化。
 *      singletonDemo = new SingletonDemo();分为三步完成
 *      1.分配对象内存空间
 *      2.初始化对象
 *      3.设置对象指向刚分配的内存地址，此时对象不为null
 *    步骤2和步骤3不存在数据依赖关系，这种重排是允许的。
 */
public class SingletonDemo {

    // volatile禁止指令重排
    private static volatile SingletonDemo singletonDemo = null;

    public SingletonDemo() {
        System.out.println(Thread.currentThread().getName() + "\t 我是构造方法");
    }

    /**
     * 懒汉模式
     * 使用DCL（Double check lock双端捡锁机制）
     * @return
     */
    public static SingletonDemo getInstance() {

        if (singletonDemo == null) {
            //加锁前后都进行检测
            synchronized (SingletonDemo.class) {
                if (singletonDemo == null) {
                    singletonDemo = new SingletonDemo();
                }
            }
        }
        return singletonDemo;
    }

    public static void main(String[] args) {

//        System.out.println(SingletonDemo.getInstance() == SingletonDemo.getInstance());
//        System.out.println(SingletonDemo.getInstance() == SingletonDemo.getInstance());
//        System.out.println(SingletonDemo.getInstance() == SingletonDemo.getInstance());

        // 多线程并发，单例模式构造方法打印多次
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                SingletonDemo.getInstance();
            }, String.valueOf(i)).start();
         }

    }
}
