package com.and.middleware.component;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.Transformations;
import android.support.annotation.Nullable;

import com.and.middleware.event.Resource;


public abstract class ObservableViewModel<Param, V> extends AndViewModel {


    private MutableLiveData<Param> liveData = new MutableLiveData<>();

    private LiveData<Resource<V>> result;


    public LiveData<Resource<V>> getResult() {
        return result;
    }


    private Param params;

    public ObservableViewModel() {
        result = Transformations.switchMap(liveData, new Function<Param, LiveData<Resource<V>>>() {
            @Override
            public LiveData<Resource<V>> apply(Param input) {
                return onCreateRequestLiveData(input);
            }
        });
        result.observeForever(new Observer<Resource<V>>() {
            @Override
            public void onChanged(@Nullable Resource<V> resource) {
                ObservableViewModel.this.onChange(resource);
            }
        });


    }

    public abstract LiveData<Resource<V>> onCreateRequestLiveData(Param param);

    public abstract void onChange(Resource<V> resource);

    @Override
    public void retry() {
        super.retry();
        notifyParamChanged();
    }


    public void setParams(Param params) {
        this.params = params;
    }

    public Param getParams() {
        return params;
    }

    public void notifyParamChanged() {
        liveData.setValue(getParams());
    }

    public void notifyParamChanged(Param params) {
        setParams(params);
        notifyParamChanged();
    }
}
