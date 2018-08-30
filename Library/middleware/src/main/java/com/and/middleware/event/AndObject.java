package com.and.middleware.event;

import android.databinding.BaseObservable;
import android.databinding.ObservableBoolean;
import android.view.View;


/**
 * Created by xianggaofeng on 2018/1/30.
 */

public class AndObject extends BaseObservable implements View.OnClickListener {

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


    @Override
    public void onClick(View v) {

    }
}
