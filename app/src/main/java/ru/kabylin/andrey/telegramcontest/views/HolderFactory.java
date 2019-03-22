package ru.kabylin.andrey.telegramcontest.views;

import android.content.Context;
import android.view.View;

public interface HolderFactory<T> {
    RecyclerItemHolder<T> create(Context context, View view);
}
