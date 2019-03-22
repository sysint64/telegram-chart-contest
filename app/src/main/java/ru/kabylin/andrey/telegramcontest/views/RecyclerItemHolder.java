package ru.kabylin.andrey.telegramcontest.views;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public abstract class RecyclerItemHolder<T> extends RecyclerView.ViewHolder {
    protected final Context context;
    protected final View view;

    public abstract void bind(T data);

    public RecyclerItemHolder(Context context, View view) {
        super(view);

        this.context = context;
        this.view = view;
    }
}
