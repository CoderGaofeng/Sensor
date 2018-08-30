//package com.and.middleware.component.lifecycle;
//
//import android.arch.lifecycle.LiveData;
//import android.arch.paging.PagedList;
//import android.support.annotation.NonNull;
//import android.support.v7.util.DiffUtil;
//
//import com.prayxiang.support.common.AppExecutors;
//import com.prayxiang.support.common.util.CallLiveData;
//import com.prayxiang.support.common.vo.Resource;
//import com.prayxiang.support.recyclerview.PagedListAdapter;
//import com.prayxiang.support.recyclerview.PagingRequestHelper;
//import com.prayxiang.support.recyclerview.tools.Cell;
//
//import java.util.List;
//
//public class PageViewModelTest<Param, T extends Cell> extends ObservableViewModel<Param, List<T>> {
//    private PagingBoundaryCallback<T> callback = new PagingBoundaryCallback<>();
//
//
//    private PagingRequestHelper.Listener listener = new PagingRequestHelper.Listener() {
//        @Override
//        public void onStatusChange(@NonNull PagingRequestHelper.StatusReport report) {
//            if(report.initial==null){
//
//            }
//        }
//    };
//
//    public PageViewModelTest() {
//        PagedListAdapter<T> adapter = new PagedListAdapter<T>(new DiffUtil.ItemCallback<T>() {
//            @Override
//            public boolean areItemsTheSame(T oldItem, T newItem) {
//                return false;
//            }
//
//            @Override
//            public boolean areContentsTheSame(T oldItem, T newItem) {
//                return false;
//            }
//        });
//        callback.helper.addListener(listener);
//
//    }
//
//
//    /**
//     * @param param
//     * @return
//     */
//    @Override
//    public LiveData<Resource<List<T>>> onCreateRequestLiveData(Param param) {
//
//
//        return null;
//    }
//
//    public <V> CallLiveData<V> onCreateCallLiveData() {
//        return null;
//    }
//
//
//    @Override
//    public void onChange(Resource<List<T>> resource) {
//
//    }
//
//    @Override
//    public void retry() {
//        super.retry();
//        callback.helper.retryAllFailed();
//    }
//
//    class PagingBoundaryCallback<T> extends PagedList.BoundaryCallback<T> {
//
//
//        PagingRequestHelper helper;
//
//
//        public PagingBoundaryCallback() {
//            this.helper = new PagingRequestHelper(AppExecutors.get().dispatchThread());
//            helper.addListener(new PagingRequestHelper.Listener() {
//                @Override
//                public void onStatusChange(@NonNull PagingRequestHelper.StatusReport report) {
//
//                }
//            });
//        }
//
//        @Override
//        public void onZeroItemsLoaded() {
//            super.onZeroItemsLoaded();
//            helper.runIfNotRunning(PagingRequestHelper.RequestType.INITIAL, new PagingRequestHelper.Request() {
//                @Override
//                public void run(Callback callback) {
//                    CallLiveData<T> liveData = onCreateCallLiveData();
//                    liveData.observeForever(tAndResponse -> {
//                        if (tAndResponse == null) {
//                            callback.recordFailure(new Throwable(""));
//                            return;
//                        }
//                        if (tAndResponse.isSuccessful()) {
//                            callback.recordSuccess();
//                        } else {
//                            callback.recordFailure(new Throwable(tAndResponse.errorMessage));
//                        }
//                    });
//                }
//            });
//        }
//
//        @Override
//        public void onItemAtEndLoaded(@NonNull T itemAtEnd) {
//            super.onItemAtEndLoaded(itemAtEnd);
//            helper.runIfNotRunning(PagingRequestHelper.RequestType.BEFORE, new PagingRequestHelper.Request() {
//                @Override
//                public void run(Callback callback) {
//                    CallLiveData<T> liveData = onCreateCallLiveData();
//                    liveData.observeForever(tAndResponse -> {
//                        if (tAndResponse == null) {
//                            callback.recordFailure(new Throwable(""));
//                            return;
//                        }
//                        if (tAndResponse.isSuccessful()) {
//                            callback.recordSuccess();
//                        } else {
//                            callback.recordFailure(new Throwable(tAndResponse.errorMessage));
//                        }
//                    });
//                }
//            });
//        }
//
//        @Override
//        public void onItemAtFrontLoaded(@NonNull T itemAtFront) {
//            super.onItemAtFrontLoaded(itemAtFront);
//            helper.runIfNotRunning(PagingRequestHelper.RequestType.AFTER, new PagingRequestHelper.Request() {
//                @Override
//                public void run(Callback callback) {
//                    CallLiveData<T> liveData = onCreateCallLiveData();
//                    liveData.observeForever(tAndResponse -> {
//                        if (tAndResponse == null) {
//                            callback.recordFailure(new Throwable(""));
//                            return;
//                        }
//                        if (tAndResponse.isSuccessful()) {
//                            callback.recordSuccess();
//                        } else {
//                            callback.recordFailure(new Throwable(tAndResponse.errorMessage));
//                        }
//                    });
//                }
//            });
//        }
//    }
//
//
//}
