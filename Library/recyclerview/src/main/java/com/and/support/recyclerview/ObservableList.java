package com.and.support.recyclerview;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ObservableList<T extends Object> extends ArrayList<Object> implements ListUpdateCallback {
    private int fixedOffset;
    protected int footOffset;
    List<ListUpdateCallback> callbacks;


    public void display(List<T> list) {
        int size = size() - fixedOffset - footOffset;
        if (size == 0) {
            replace(list);
        } else {
            insert(list);
        }

    }

    public boolean isEmpty() {
        int size = size() - fixedOffset - footOffset;
        return size == 0;
    }

    public void replace(List<T> list) {
        if (list == null) {
            list = Collections.emptyList();
        }
        clearRealData(footOffset, true);
        addAll(size() - footOffset, list);
    }

    public void replace(List<T> list, boolean anim) {
        if (list == null) {
            list = Collections.emptyList();
        }
        clearRealData(footOffset, anim);
        if (anim) {
            addAll(size() - footOffset, list);
        } else {
            super.addAll(size() - footOffset, list);
            onChanged();
        }

    }


    public void insert(List<T> list) {
        addAll(size() - footOffset, list);
    }


    public void insert(T t) {
        add(size() - footOffset, t);
    }

    public void addFixed(Object object) {
        add(fixedOffset, object);
        onInserted(fixedOffset, 1);
        fixedOffset++;
    }

    public void removeFixed(Object object) {
        boolean b = remove(object);
        if (b) {
            fixedOffset--;
        }
    }

    public void addFooter(Object object) {
        add(size(), object);
        footOffset++;
        onInserted(size() - 1, 1);
    }

    public void removeFooter(Object object) {
        boolean b = remove(object);
        if (b) {
            footOffset--;
        }

    }

    private void clearRealData(int offset, boolean anim) {
        int size = size();
        if (anim) {
            for (int i = size - offset - 1; i >= fixedOffset; i--) {
                remove(i);
            }
        } else {
            for (int i = size - offset - 1; i >= fixedOffset; i--) {
                super.remove(i);
            }
        }

    }

    public List getRealData() {
        int size = size();
        return subList(fixedOffset, size - footOffset);
    }

    public void addOnListChangeCallback(ListUpdateCallback callback) {
        if (callbacks == null) {
            callbacks = new ArrayList<>();
        }
        callbacks.add(callback);
    }

    public void removeListChangeCallback(ListUpdateCallback callback) {
        if (callbacks != null) {
            callbacks.remove(callback);
        }
    }

    public void removeAllChangeCallback() {
        if (callbacks != null) {
            callbacks.clear();
        }
    }

    @Override
    public void onInserted(int position, int count) {
        if (callbacks != null) {
            for (ListUpdateCallback callback :
                    callbacks
                    ) {
                callback.onInserted(position, count);
            }
        }
    }

    @Override
    public void onRemoved(int position, int count) {
        if (callbacks != null) {
            for (ListUpdateCallback callback :
                    callbacks
                    ) {
                callback.onRemoved(position, count);
            }
        }
    }

    @Override
    public void onMoved(int fromPosition, int toPosition) {
        if (callbacks != null) {
            for (ListUpdateCallback callback :
                    callbacks
                    ) {
                callback.onMoved(fromPosition, toPosition);
            }
        }
    }

    @Override
    public void onChanged(int position, int count, Object payload) {
        if (callbacks != null) {
            for (ListUpdateCallback callback :
                    callbacks
                    ) {
                callback.onChanged(position, count, payload);
            }
        }
    }

    @Override
    public void onChanged() {
        if (callbacks != null) {
            for (ListUpdateCallback callback :
                    callbacks
                    ) {
                callback.onChanged();
            }
        }

    }


    @Override
    public boolean add(Object object) {
        super.add(object);
        onInserted(size() - 1, 1);
        return true;
    }

    @Override
    public void add(int index, Object object) {
        super.add(index, object);
        onInserted(index, 1);
    }

    @Override
    public boolean addAll(Collection<? extends Object> collection) {
        int oldSize = size();
        boolean added = super.addAll(collection);
        if (added) {
            onInserted(oldSize, size() - oldSize);
        }
        return added;
    }

    @Override
    public boolean addAll(int index, Collection<? extends Object> collection) {
        boolean added = super.addAll(index, collection);
        if (added) {
            onInserted(index, collection.size());
        }
        return added;
    }

    @Override
    public void clear() {
        int oldSize = size();
        super.clear();
        fixedOffset = 0;
        footOffset = 0;
        if (oldSize != 0) {
            onRemoved(0, oldSize);
        }
    }

    @Override
    public Object remove(int index) {
        Object val = super.remove(index);
        onRemoved(index, 1);
        return val;
    }

    @Override
    public boolean remove(Object object) {
        int index = indexOf(object);
        if (index >= 0) {
            remove(index);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Object set(int index, Object object) {
        Object val = super.set(index, object);
        onChanged(index, 1, object);

        return val;
    }

    @Override
    public void removeRange(int fromIndex, int toIndex) {
        super.removeRange(fromIndex, toIndex);
        onRemoved(fromIndex, toIndex - fromIndex);
    }

    public void move(int fromPosition, int toPosition) {
        Collections.swap(this, fromPosition, toPosition);
        onMoved(fromPosition, toPosition);
    }

    public void setSource(List list) {
        super.clear();
        super.addAll(list);
    }
}
