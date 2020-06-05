package com.example.demo.cas;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author xsx
 *
 * 理解原子引用
 */
public class AtomicReferenceDemo {

    public static void main(String[] args) {
        User z3 = new User("Z3", 25);
        User li4 = new User("li4", 25);

        AtomicReference<User> atomicReference = new AtomicReference<>();
        atomicReference.set(z3);

        System.out.println(atomicReference.compareAndSet(z3, li4) + "\t" + atomicReference.get().toString());
        System.out.println(atomicReference.compareAndSet(z3, li4) + "\t" + atomicReference.get().toString());
    }

}

@Getter
@ToString
@AllArgsConstructor
class User {
    String userName;
    int age;
}
