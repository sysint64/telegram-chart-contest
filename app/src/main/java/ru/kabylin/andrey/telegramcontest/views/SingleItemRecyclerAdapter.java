package ru.kabylin.andrey.telegramcontest.views;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.View;

import java.util.List;

public class SingleItemRecyclerAdapter<T> extends ItemRecyclerAdapter<T> {
    private final int layout;
    private final HolderFactory<T> holderFactory;

    public SingleItemRecyclerAdapter(Context context, List<T> items, @LayoutRes int layout, HolderFactory<T> holderFactory) {
        super(context, items);
        this.layout = layout;
        this.holderFactory = holderFactory;
    }

    @Override
    int obtainLayout(int viewType) {
        return layout;
    }

    @Override
    RecyclerItemHolder<T> obtainHolder(int viewType, View view) {
        return holderFactory.create(context, view);
    }
}
