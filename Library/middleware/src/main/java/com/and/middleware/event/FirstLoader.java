//package com.and.middleware.event;
//
//import java.util.Collection;
//
///**
// * Created by xianggaofeng on 2017/12/13.
// */
//
//public class FirstLoader extends Loader {
//
//    private RetryCallback callback;
//
//    public FirstLoader(RetryCallback callback) {
//        this.callback = callback;
//        mark(0);
//    }
//
//    public FirstLoader() {
//        mark(0);
//    }
//
//
//    @Override
//    protected void dispatchStatus(Resource<? extends Collection> resource) {
//        super.dispatchStatus(resource);
//    }
//
//    @Override
//    public void retry() {
//        LoaderStatus status = getStatus();
//        if (status != LoaderStatus.GONE && status != LoaderStatus.LOADING && isEnabled()) {
//            if (loadListener != null) {
//                setStatus(LoaderStatus.LOADING);
//                loadListener.load(currentPage);
//            }
//            if (callback != null) {
//                setStatus(LoaderStatus.LOADING);
//                callback.retry();
//            }
//        }
//    }
//}
