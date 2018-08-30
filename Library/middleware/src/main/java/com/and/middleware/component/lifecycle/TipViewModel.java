package com.and.middleware.component.lifecycle;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.databinding.PropertyChangeRegistry;

import com.and.middleware.AppExecutors;
import com.and.middleware.event.Tip;

/**
 * Created by xianggaofeng on 2018/1/30.
 */

public class TipViewModel extends ViewModel {
    private transient PropertyChangeRegistry mCallbacks;

    private SingleLiveEvent<String> messageEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<Tip> tipEvent = new SingleLiveEvent<>();


    public LiveData<String> obtainMessageEvent() {
        return messageEvent;
    }

    public LiveData<Tip> obtainTipEvent() {
        return tipEvent;
    }

    public void tip(int type, String message) {
        tipEvent.setValue(new Tip(type, message));
    }

    public void postTip(int type, String message) {
        tipEvent.postValue(new Tip(type, message));
    }

    public void message(String message) {
        messageEvent.postValue(message);
    }


    public void postSuccess(String message) {
        postTip(Tip.TYPE_SUCCESS, message);
    }

    public void success(String message) {
        tip(Tip.TYPE_SUCCESS, message);
    }

    public void error(String message) {
        tip(Tip.TYPE_FAIL, message);
    }

    public void postError(String message) {
        postTip(Tip.TYPE_FAIL, message);
    }


    public void loading(String message) {
        tip(Tip.TYPE_LOADING, message);
    }

    public void postLoading(String message) {
        postTip(Tip.TYPE_LOADING, message);
    }

    public void loading() {
        tip(Tip.TYPE_LOADING, null);
    }

    public void dismiss() {
        assertMainThread("dismiss");

        tipEvent.setValue(new Tip(Tip.TYPE_DISMISS, null));

    }

    protected static void assertMainThread(String methodName) {
        if (!AppExecutors.get().isMainThread()) {
            throw new IllegalStateException("Cannot invoke " + methodName + " on a background"
                    + " thread");
        }
    }

    public void postDismiss() {
        tip(Tip.TYPE_DISMISS, null);
    }

}
