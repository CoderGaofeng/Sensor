/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.and.middleware;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


/**
 * Global executor pools for the whole application.
 * <p>
 * Grouping tasks like this avoids the effects of task starvation (e.g. disk reads don't wait behind
 * webservice requests).
 */
public class AppExecutors {

    private static volatile AppExecutors executors = new AppExecutors();

    public static AppExecutors get() {
        if (executors == null) {
            synchronized (AppExecutors.class) {
                if (executors == null) {
                    return new AppExecutors();
                }
            }
        }
        return executors;
    }

    private final Executor diskIO;

    private final Executor networkIO;
    private final DispatchThreadExecutor dispatchThread;

    private final MainThreadExecutor mainThread;

    public AppExecutors(Executor diskIO, Executor networkIO, Executor mainThread, Executor dispatchThread) {
        this.diskIO = diskIO;
        this.networkIO = networkIO;
        this.mainThread = (MainThreadExecutor) mainThread;
        this.dispatchThread = (DispatchThreadExecutor) dispatchThread;
    }


    public AppExecutors() {
        this(Executors.newSingleThreadExecutor(), Executors.newFixedThreadPool(3),
                new MainThreadExecutor(), new DispatchThreadExecutor());
    }

    public Executor diskIO() {
        return diskIO;
    }

    public Executor networkIO() {
        return networkIO;
    }

    public MainThreadExecutor mainThread() {
        return mainThread;
    }

    public DispatchThreadExecutor dispatchThread() {
        return dispatchThread;
    }

    public static class MainThreadExecutor implements Executor {
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHandler.post(command);
        }


        public void execute(@NonNull Runnable command, long delay) {
            mainThreadHandler.postDelayed(command, delay);
        }
    }

    public static class DispatchThreadExecutor implements Executor {
        private HandlerThread dispatchThread = new HandlerThread("dispatch");
        private Handler dispatchThreadHandler = new Handler(dispatchThread.getLooper());

        public DispatchThreadExecutor() {
            dispatchThread.start();
        }

        @Override
        public void execute(@NonNull Runnable command) {
            dispatchThreadHandler.post(command);
        }


        public void execute(@NonNull Runnable command, long delay) {
            dispatchThreadHandler.postDelayed(command, delay);
        }
    }

    public boolean isMainThread() {
        return Looper.getMainLooper().getThread() == Thread.currentThread();
    }
}
