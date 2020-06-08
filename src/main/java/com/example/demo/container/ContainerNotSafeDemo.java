package com.example.demo.container;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author xsx
 * List集合类不安全问题
 *
 * ArrayList
 */
public class ContainerNotSafeDemo {

    public static void main(String[] args) {

        // 线程不安全
        List<String> list = new ArrayList<>();
        // 线程安全
        List<String> list1 = new Vector<>();
        // 线程安全
        List<String> list2 = Collections.synchronizedList(new ArrayList<>());

        // 写时分离
        List<String> list3 = new CopyOnWriteArrayList<>();

        for (int i = 0; i < 30; i++) {
            new Thread(() -> {
                list.add(UUID.randomUUID().toString().substring(0, 8));
                System.out.println(list);
            }, String.valueOf(i)).start();
        }

        //ConcurrentModificationException 并发修改异常
        /**
         * 1.故障现象
         *  ConcurrentModificationException
         *
         *  2.导致原因
         *      并发争抢修改导致。当前线程正在写入。另一个线程抢夺写入。导致数据不一致异常。并发修改异常
         *  3.解决方案
         *      3.1 new Vector<>();
         *      3.2 Collections.synchronizedList(new ArrayList<>());
         *      3.3 new CopyOnWriteArrayList<>();
         *  4.优化建议
         *
         */

        /**
         * 写时复制：
         * 优点是可以对容器进行并发的读，而不需要加锁。因为当前容器不会添加任何的元素，所有CopyOnWrite容器也是一种
         * 读写分离的思想，读和写不同的容器
         * public boolean add(E e) {
         *         final ReentrantLock lock = this.lock;
         *         lock.lock();
         *         try {
         *             Object[] elements = getArray();
         *             int len = elements.length;
         *             Object[] newElements = Arrays.copyOf(elements, len + 1);
         *             newElements[len] = e;
         *             setArray(newElements);
         *             return true;
         *         } finally {
         *             lock.unlock();
         *         }
         *     }
         */
    }
}
