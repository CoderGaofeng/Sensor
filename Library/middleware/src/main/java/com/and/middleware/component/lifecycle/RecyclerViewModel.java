//package com.and.middleware.component.lifecycle;
//
//import android.arch.core.util.Function;
//import android.arch.lifecycle.LiveData;
//import android.arch.lifecycle.MutableLiveData;
//import android.arch.lifecycle.Observer;
//import android.arch.lifecycle.Transformations;
//import android.support.annotation.Nullable;
//import android.support.v7.widget.RecyclerView;
//
//import com.prayxiang.support.common.recyclerview.LoadListener;
//import com.prayxiang.support.common.recyclerview.LoadMoreScrollListener;
//import com.prayxiang.support.common.recyclerview.RecyclerViewBinder;
//import com.prayxiang.support.common.vo.FirstLoader;
//import com.prayxiang.support.common.vo.LastLoader;
//import com.prayxiang.support.common.vo.Resource;
//import com.prayxiang.support.common.vo.Status;
//import com.prayxiang.support.recyclerview.ObservableAdapter;
//import com.prayxiang.support.recyclerview.ObservableList;
//
//import java.util.Collections;
//import java.util.List;
//
//
//public abstract class RecyclerViewModel<Body, V> extends ObservableViewModel<Body, List<V>> implements LoadListener, RecyclerViewBinder {
//
//
//    private int currentPage;
//
//    public void setCurrentPage(int currentPage) {
//        this.currentPage = currentPage;
//    }
//
//    public int getCurrentPage() {
//        return currentPage;
//    }
//
//    private ObservableList items;
//    private LastLoader lastLoader = new LastLoader();
//    private FirstLoader firstLoader = new FirstLoader(this);
//    private LoadMoreScrollListener scrollListener = new LoadMoreScrollListener(lastLoader);
//
//    private boolean enableFirstLoader = true;
//    private boolean enableLastLoader = true;
//    private boolean alwaysShowLast = false;
//
//    public void setEnableFirstLoader(boolean enableFirstLoader) {
//        this.enableFirstLoader = enableFirstLoader;
//        firstLoader.setEnabled(enableFirstLoader);
//    }
//
//    public void setEnableLastLoader(boolean enableLastLoader) {
//        this.enableLastLoader = enableLastLoader;
//        lastLoader.setEnabled(enableLastLoader);
//    }
//
//
//    public void setAlwaysShowLast(boolean alwaysShowLast) {
//        this.alwaysShowLast = alwaysShowLast;
//    }
//
//    private int limit;
//    private MutableLiveData<Body> nextLiveData = new MutableLiveData<>();
//    private LiveData<Resource<List<V>>> next;
//
//    public RecyclerViewModel() {
//
//        items = generateDefaultItems();
//        next = Transformations.switchMap(nextLiveData, new Function<Body, LiveData<Resource<List<V>>>>() {
//            @Override
//            public LiveData<Resource<List<V>>> apply(Body input) {
//                return onNextRequestLiveData(input);
//            }
//        });
//        next.observeForever(new Observer<Resource<List<V>>>() {
//            @Override
//            public void onChanged(@Nullable Resource<List<V>> listResource) {
//                RecyclerViewModel.this.onNextChange(listResource);
//            }
//        });
//        lastLoader.setLoadListener(this);
//    }
//
//    public ObservableList generateDefaultItems() {
//        return new ObservableList();
//    }
//
//    public void limit(int limit) {
//        this.limit = limit;
//        lastLoader.limit(limit);
//    }
//
//    public LiveData<Resource<List<V>>> getNext() {
//        return next;
//    }
//
//    public LiveData<Resource<List<V>>> onNextRequestLiveData(Body param) {
//        return onCreateRequestLiveData(param);
//    }
//
//    public void onNextChange(Resource<List<V>> resource) {
//        if (enableLastLoader) {
//            lastLoader.setResource(resource);
//        }
//        List list = resource.data;
//        if (list == null) {
//            list = Collections.emptyList();
//        }
//        items.insert(list);
//    }
//
//
//    @Override
//    public void onChange(Resource<List<V>> resource) {
//        List list = resource.data;
//        if (list == null) {
//            list = Collections.emptyList();
//        }
//        int size = list.size();
//
//        if (resource.status == Status.LOADING) {
//            setRefreshing(true);
//            if (size == 0) {
//                if (enableFirstLoader) {
//                    firstLoader.setResource(resource);
//                    if (!firstLoader.isAttached() && items.getRealData().size() == 0) {
//                        items.replace(Collections.singletonList(firstLoader), false);
//                        firstLoader.setAttached(true);
//                        return;
//                    }
//                }
//                return;
//            }
//
//        } else {
//            setRefreshing(false);
//        }
//
//        if (list.size() == 0) {
//            if (lastLoader.isAttached()) {
//                items.removeFooter(lastLoader);
//                lastLoader.resetDefault();
//                lastLoader.setAttached(false);
//            }
//
//            if (enableFirstLoader) {
//                firstLoader.setResource(resource);
//
//                if (!firstLoader.isAttached()) {
//                    items.replace(Collections.singletonList(firstLoader), false);
//                    firstLoader.setAttached(true);
//                    return;
//                }
//            }
//        } else {
//            if (firstLoader.isAttached()) {
//                items.remove(firstLoader);
//                firstLoader.setAttached(false);
//            }
//            if (size < limit) {
//                items.replace(list, false);
//                if (alwaysShowLast) {
//                    lastLoader.setResource(resource);
//                    if (!lastLoader.isAttached()) {
//                        items.addFooter(lastLoader);
//                        lastLoader.setAttached(true);
//                    }
//                }
//
//            } else {
//                items.replace(list, false);
//                if ((enableLastLoader)) {
//                    lastLoader.setResource(resource);
//                    if (!lastLoader.isAttached()) {
//                        items.addFooter(lastLoader);
//                        lastLoader.setAttached(true);
//                    }
//                }
//
//            }
//
//        }
//
//    }
//
//    public void fetchNextPage() {
//        nextLiveData.setValue(getParams());
//    }
//
//
//    @Override
//    public void load(int offset) {
//        setCurrentPage(offset);
//        loadPage(offset);
//        fetchNextPage();
//    }
//
//    @Override
//    public void retry() {
//        setCurrentPage(0);
//        loadPage(0);
//        super.retry();
//    }
//
//    public void loadPage(int page) {
//
//    }
//
//
//    /**
//     * recyclerView 相关
//     *
//     * @return
//     */
//    public RecyclerView.OnScrollListener getScrollListener() {
//        return scrollListener;
//    }
//
//    public ObservableList getItems() {
//        return items;
//    }
//
//
//    private ObservableAdapter adapter;
//
//    public ObservableAdapter getAdapter() {
//        if (adapter == null) {
//            adapter = generateDefaultAdapter();
//            onBindAdapter(adapter);
//        }
//        return adapter;
//    }
//
//    protected ObservableAdapter generateDefaultAdapter() {
//        return new ObservableAdapter(items, getScrollListener());
//    }
//
//    protected void onBindAdapter(ObservableAdapter adapter) {
//
//    }
//
//    protected void onBindRecyclerView(RecyclerView recyclerView) {
//
//    }
//
//
//    @Override
//    public void onBind(RecyclerView recyclerView) {
//        recyclerView.setAdapter(getAdapter());
//        onBindRecyclerView(recyclerView);
//    }
//
//
//
//}
