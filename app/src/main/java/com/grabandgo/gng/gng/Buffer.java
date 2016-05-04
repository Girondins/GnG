package com.grabandgo.gng.gng;

import java.util.LinkedList;

/**
 * Buffer class.
 * @param <T> - Generic
 */
public class Buffer<T> {
    private LinkedList<T> buffer = new LinkedList<T>();

    public synchronized void put(T element) {
        buffer.addLast(element);
        notifyAll();
    }

    public synchronized T get() throws InterruptedException {
        while (buffer.isEmpty()) {
            wait();
        }
        return buffer.removeFirst();
    }
}
