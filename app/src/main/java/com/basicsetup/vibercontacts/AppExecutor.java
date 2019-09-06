package com.clevertrap.contactsfromviber;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class AppExecutor {

    public interface IExecutorFinished{
        void executorFinished();
    }

    private final Executor loggerIO;

    private final Executor diskIO;

    private final Executor networkIO;

    private final Executor mainThread;

    private AppExecutor(Executor diskIO, Executor networkIO, Executor loggerIO, Executor mainThread) {
        this.diskIO = diskIO;
        this.networkIO = networkIO;
        this.mainThread = mainThread;
        this.loggerIO = loggerIO;
    }

    private static AppExecutor INSTANCE = null;

    public static AppExecutor getINSTANCE(){

        if(INSTANCE == null){
            INSTANCE = new AppExecutor(Executors.newFixedThreadPool(2), Executors.newFixedThreadPool(3),
                    Executors.newFixedThreadPool(1),
                    new MainThreadExecutor());
        }
        return INSTANCE;
    }

    private static class MainThreadExecutor implements Executor {
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());
        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHandler.post(command);
        }
    }

    public Executor getDiskIO() {
        return diskIO;
    }

    public Executor getNetworkIO() {
        return networkIO;
    }

    public Executor getMainThread() {
        return mainThread;
    }

    public Executor getLoggerIO() {
        return loggerIO;
    }
}
