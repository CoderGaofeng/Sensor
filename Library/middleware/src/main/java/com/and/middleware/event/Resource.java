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

package com.and.middleware.event;


import android.arch.lifecycle.MediatorLiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.and.middleware.event.Status.ERROR;
import static com.and.middleware.event.Status.LOADING;
import static com.and.middleware.event.Status.SUCCESS;


/**
 * A generic class that holds a value with its loading status.
 *
 * @param <T>
 */
public class Resource<T> {

    @NonNull
    public final Status status;

    @Nullable
    public final String message;

    @Nullable
    public T data;

    public int code;

    public Resource(@NonNull Status status, @Nullable T data, @Nullable String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public Resource(@NonNull Status status, @Nullable T data, int code, @Nullable String message) {
        this.status = status;
        this.data = data;
        this.message = message;
        this.code = code;
    }

    public Resource(Resource resource) {
        this.status = resource.status;
        this.message = resource.message;
        this.code = resource.code;
    }

    @Override
    public String toString() {
        return "Resource{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", data=" + data +
                ", code=" + code +
                '}';
    }

    public static <T> Resource<T> success(@Nullable T data) {
        return new Resource<>(SUCCESS, data, null);
    }

    public static <T> Resource<T> error(String msg, @Nullable T data) {
        return new Resource<>(ERROR, data, -1, msg);
    }

    public static <T> Resource<T> error(int code, String msg, @Nullable T data) {
        return new Resource<>(ERROR, data, code, msg);
    }

    public static <T> Resource<T> loading(@Nullable T data) {
        return new Resource<>(LOADING, data, null);
    }

    public static <T> MediatorLiveData<Resource<T>> errorLiveData(String msg, T data) {
        MediatorLiveData<Resource<T>> liveData = new MediatorLiveData<>();
        liveData.postValue(new Resource<>(ERROR, data, -1, msg));
        return liveData;
    }
}
