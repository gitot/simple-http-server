package com.gyw.server.http.exceutor;

import java.net.Socket;
import java.util.concurrent.*;

public enum Executor {
    INSTANCE;

    private BlockingQueue queue = new LinkedBlockingQueue();
    private ThreadFactory factory = new InternalThreadFactory();
    private ExecutorService executor;


    Executor() {
        int cpus = Runtime.getRuntime().availableProcessors();
        //io与cpu耗时比
        int percent = 3;
        executor = new ThreadPoolExecutor(cpus * percent + 1,
                15,
                1000,
                TimeUnit.SECONDS,
                queue,
                factory);
    }

    public void execute(Runnable task) {
        executor.execute(task);
    }


    static class InternalThreadFactory implements ThreadFactory {
        @Override
        public Thread newThread(Runnable r) {
            //TODO to be externed
            return new Thread(r);
        }
    }
}
