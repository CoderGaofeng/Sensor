package com.and.middleware.component.lifecycle;

import android.arch.lifecycle.LiveData;

import com.and.middleware.event.Resource;


/**
 * Created by xianggaofeng on 2018/1/27.
 */

public class ErrorLiveData extends LiveData {
    private ErrorLiveData() {
        postValue(Resource.error("",null));
    }

    public static <T> LiveData<T> create() {
        //noinspection unchecked
        return new ErrorLiveData();
    }
}
