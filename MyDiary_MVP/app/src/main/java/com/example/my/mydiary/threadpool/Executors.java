package com.example.my.mydiary.threadpool;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author liushilin Created at 15:43
 */

public class Executors {

    private static final String TAG = "Executors";

    private static int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();

    private static int KEEP_ALIVE_TIME = 1;

    private static TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;

    private static BlockingQueue<Runnable> sTaskQueue = new LinkedBlockingQueue<>();


    private static ExecutorService sTaskThread = new ThreadPoolExecutor(NUMBER_OF_CORES,
            NUMBER_OF_CORES * 2, KEEP_ALIVE_TIME, KEEP_ALIVE_TIME_UNIT, sTaskQueue,
            new BackgroundThreadFactory(), new DefaultRejectedExecutionHandler());

    private static Executor sMainThread = new MainThreadExecutor();

    private static class BackgroundThreadFactory implements ThreadFactory {

        private static final AtomicInteger POOL_NUMBER = new AtomicInteger(1);
        private static final int THREAD_PRIORITY = 5;
        private final ThreadGroup mGroup;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String mNamePrefix;

        @Override
        public Thread newThread(@NonNull Runnable r) {
            Thread thread = new Thread(this.mGroup, r, this.mNamePrefix + this.threadNumber.getAndIncrement(), 0L);
            if (thread.isDaemon()) {
                thread.setDaemon(false);
            }

            if (thread.getPriority() != THREAD_PRIORITY) {
                thread.setPriority(THREAD_PRIORITY);
            }

            return thread;
        }

        public BackgroundThreadFactory() {
            SecurityManager securityManager = System.getSecurityManager();
            this.mGroup = securityManager != null ? securityManager.getThreadGroup() : Thread.currentThread().getThreadGroup();
            this.mNamePrefix = "IPTV-pool-" + POOL_NUMBER.getAndIncrement() + "-thread-";
        }
    }

    private static class DefaultRejectedExecutionHandler implements RejectedExecutionHandler {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
//            LogUtils.d(TAG, "rejectedExecution: ");
        }
    }

    private static class MainThreadExecutor implements Executor {
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHandler.post(command);
        }
    }

    public static ExecutorService getTaskThread() {
        return sTaskThread;
    }

    public static Executor getMainThread() {
        return sMainThread;
    }
}
