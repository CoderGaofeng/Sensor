package com.and.support.recyclerview;

public interface DiffCallback<T> {
        boolean areItemsTheSame(T oldItem, T newItem);

        boolean areContentsTheSame(T oldItem, T newItem);

    }