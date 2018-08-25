package com.example.my.mydiary.threadpool;

/**
 * @author ggz
 * @date 18年8月13日
 */
public class ThreadPool {

    private volatile static ThreadPool instance = null;

    private ThreadPool() {
    }

    public static ThreadPool getInstance() {
        if (instance == null) {
            synchronized (ThreadPool.class) {
                if (instance == null) {
                    instance = new ThreadPool();
                }
            }
        }
        return instance;
    }
}
