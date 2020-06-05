package com.example.demo.container;

import java.util.*;

/**
 * @author xsx
 * 集合类不安全问题
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
         *
         *  3.解决方案
         *      3.1 new Vector<>();
         *      3.2 Collections.synchronizedList(new ArrayList<>());
         *  4.优化建议
         *
         *
         *
         */
    }
}
