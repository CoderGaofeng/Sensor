//package com.and.middleware.event;
//
//import android.databinding.BaseObservable;
//import android.databinding.Bindable;
//
//
//import java.util.Collection;
//
//
///**
// * Created by xianggaofeng on 2017/6/6.
// */
//public class Loader extends BaseObservable implements RetryCallback {
//
//
//    private LoaderStatus status = LoaderStatus.DEFAULT;
//    private int defaultPage = 1;
//    protected int currentPage = defaultPage;
//    private boolean enabled = true;
//    private boolean attached =false;
//    private String errorMessage;
//
//    public void setCurrentPage(int currentPage) {
//        this.currentPage = currentPage;
//    }
//
//    public int getCurrentPage() {
//        return currentPage;
//    }
//
//    public void mark(int page) {
//        if (currentPage == defaultPage) {
//            currentPage = page;
//        }
//        this.defaultPage = page;
//    }
//
//    public Loader() {
//
//    }
//
//    @Bindable
//    public LoaderStatus getStatus() {
//        return status;
//    }
//
//    public void setStatus(LoaderStatus status) {
//        if (this.status != status) {
//            this.status = status;
//            if (status == LoaderStatus.SUCCESS) {
//                currentPage++;
//            }
//            notifyPropertyChanged(BR.status);
//        }
//    }
//
//
//    protected void dispatchStatus(Resource<? extends Collection> resource) {
//        Status status = resource.status;
//        LoaderStatus loaderStatus;
//        if (status == Status.LOADING) {
//            loaderStatus = LoaderStatus.LOADING;
//        } else if (status == Status.ERROR) {
//            loaderStatus = LoaderStatus.ERROR;
//        } else if (status == Status.SUCCESS) {
//            if (resource.data == null || resource.data.size() == 0) {
//                if (currentPage == 0) {
//                    loaderStatus = LoaderStatus.EMPTY;
//                } else {
//                    loaderStatus = LoaderStatus.GONE;
//                }
//
//            } else {
//                loaderStatus = LoaderStatus.SUCCESS;
//            }
//        } else {
//            loaderStatus = LoaderStatus.DEFAULT;
//        }
//
//        setStatus(loaderStatus);
//
//    }
//
//    final public void setResource(Resource<? extends Collection> resource) {
//        dispatchStatus(resource);
//    }
//
//    public void reset() {
//        setStatus(LoaderStatus.DEFAULT);
//        currentPage = defaultPage;
//        enabled = false;
//    }
//
//
//    public void resetDefault() {
//        setStatus(LoaderStatus.DEFAULT);
//        currentPage = defaultPage;
//    }
//
//
//    public boolean isEnabled() {
//        return enabled;
//    }
//
//    public void setEnabled(boolean enabled) {
//        this.enabled = enabled;
//    }
//
//    public void setAttached(boolean attached) {
//        this.attached = attached;
//    }
//
//    public boolean isAttached() {
//        return attached;
//    }
//
//    protected LoadListener loadListener;
//
//    public void setLoadListener(LoadListener loadListener) {
//        this.loadListener = loadListener;
//    }
//
//    public LoadListener getLoadListener() {
//        return loadListener;
//    }
//
//    @Override
//    public void retry() {
//        if (status != LoaderStatus.GONE && status != LoaderStatus.LOADING && isEnabled()) {
//            if (loadListener != null) {
//                setStatus(LoaderStatus.LOADING);
//                loadListener.load(currentPage);
//            }
//        }
//    }
//}
