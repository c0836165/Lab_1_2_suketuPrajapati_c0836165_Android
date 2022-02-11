package com.example.lab_1_2_suketuprajapati_c0836165_android.database;

import android.os.Handler;
import android.os.Looper;


import androidx.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class ProductRepo {
    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static ProductRepo sInstance;
    private final Executor diskIO;
    private final Executor mainThread;
    private final Executor networkIO;


    public ProductRepo(Executor diskIO, Executor mainThread, Executor networkIO) {
        this.diskIO = diskIO;
        this.mainThread = mainThread;
        this.networkIO = networkIO;
    }

    public static ProductRepo getInstance() {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new ProductRepo(Executors.newSingleThreadExecutor(),
                        Executors.newFixedThreadPool(3),
                        new MainThreadExecutor());
            }
        }
        return sInstance;
    }

    public Executor diskIO() {
        return diskIO;
    }

    public Executor mainThread() {
        return mainThread;
    }

    public Executor networkIO() {
        return networkIO;
    }

    private static class MainThreadExecutor implements Executor {
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHandler.post(command);
        }
    }
}




