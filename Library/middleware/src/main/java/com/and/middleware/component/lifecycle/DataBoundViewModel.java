package com.and.middleware.component.lifecycle;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.databinding.Bindable;
import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.PropertyChangeRegistry;
import android.support.annotation.NonNull;

import com.and.middleware.AppExecutors;
import com.and.middleware.BR;
import com.and.middleware.event.Resource;
import com.and.middleware.event.RetryCallback;
import com.and.middleware.event.Status;
import com.and.middleware.event.Tip;

/**
 * Created by xianggaofeng on 2018/1/30.
 */

public class DataBoundViewModel extends ViewModel implements RetryCallback, Observable {
    private String title;

    private String key;

    private transient PropertyChangeRegistry mCallbacks;


    @Override
    public void addOnPropertyChangedCallback(@NonNull Observable.OnPropertyChangedCallback callback) {
        synchronized (this) {
            if (mCallbacks == null) {
                mCallbacks = new PropertyChangeRegistry();
            }
        }
        mCallbacks.add(callback);
    }

    @Override
    public void removeOnPropertyChangedCallback(@NonNull Observable.OnPropertyChangedCallback callback) {
        synchronized (this) {
            if (mCallbacks == null) {
                return;
            }
        }
        mCallbacks.remove(callback);
    }

    /**
     * Notifies listeners that all properties of this instance have changed.
     */
    public void notifyChange() {
        synchronized (this) {
            if (mCallbacks == null) {
                return;
            }
        }
        mCallbacks.notifyCallbacks(this, 0, null);
    }

    /**
     * Notifies listeners that a specific property has changed. The getter for the property
     * that changes should be marked with {@link Bindable} to generate a field in
     * <code>BR</code> to be used as <code>fieldId</code>.
     *
     * @param fieldId The generated BR id for the Bindable field.
     */
    public void notifyPropertyChanged(int fieldId) {
        synchronized (this) {
            if (mCallbacks == null) {
                return;
            }
        }
        mCallbacks.notifyCallbacks(this, fieldId, null);
    }


    /**
     * 刷新行为
     */
    @Override
    public void retry() {
        setRefreshing(true);
    }

    /**
     * 首次初始化时行为
     */
    public void start() {
        retry();
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


    @Bindable
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        notifyPropertyChanged(BR.title);
    }

    public void updateResource(Resource<?> resource) {
        if (resource != null && resource.status == Status.LOADING) {
            setRefreshing(true);
        } else {
            setRefreshing(false);
        }
    }

    private transient ObservableBoolean observableSelected = new ObservableBoolean(true);
    private transient ObservableBoolean observableChecked = new ObservableBoolean(true);
    private transient ObservableBoolean observableFocused = new ObservableBoolean(true);
    private transient ObservableBoolean observableEnabled = new ObservableBoolean(true);
    private transient ObservableBoolean observableChanged = new ObservableBoolean(false);
    private transient ObservableBoolean observableRefreshing = new ObservableBoolean(false);


    public ObservableBoolean getSelected() {
        return observableSelected;
    }

    public boolean isSelected() {
        return observableSelected.get();
    }

    public void setSelected(boolean selected) {
        observableSelected.set(selected);
    }

    public ObservableBoolean getChecked() {
        return observableChecked;
    }

    public boolean isChecked() {
        return observableChecked.get();
    }

    public void setChecked(boolean checked) {
        observableChecked.set(checked);
    }


    public ObservableBoolean getObservableEnabled() {
        return observableEnabled;
    }

    public boolean isEnabled() {
        return observableEnabled.get();
    }

    public void setEnabled(boolean enabled) {
        observableEnabled.set(enabled);
    }


    public ObservableBoolean getObservableFocused() {
        return observableFocused;
    }

    public boolean isFocused() {
        return observableFocused.get();
    }

    public void setFocused(boolean focused) {
        observableFocused.set(false);
    }


    public ObservableBoolean getObservableChanged() {
        return observableChanged;
    }

    public boolean isChanged() {
        return observableChanged.get();
    }

    public void setChanged(boolean changed) {
        observableChanged.set(changed);
    }


    public ObservableBoolean getObservableRefreshing() {
        return observableRefreshing;
    }


    public boolean isRefreshing() {
        return observableRefreshing.get();
    }


    public void setRefreshing(boolean refreshing) {
        observableRefreshing.set(refreshing);
    }


    /****
     *
     * tip
     *
     */

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
