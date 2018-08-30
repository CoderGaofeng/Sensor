package com.and.middleware.event;

/**
 * Created by xianggaofeng on 2017/12/14.
 */

public class CheckBean<T> extends AndObject {
    private T object;
    private String title;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public CheckBean(T object, String title) {
        this.object = object;
        this.title = String.valueOf(title);
    }

    public CheckBean(T object) {
        this.object = object;
        this.title = String.valueOf(object);
    }

    public T getItem() {
        return object;
    }
}
