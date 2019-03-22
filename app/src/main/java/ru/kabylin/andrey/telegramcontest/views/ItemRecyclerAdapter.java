package ru.kabylin.andrey.telegramcontest.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public abstract class ItemRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerItemHolder<T>> {
    protected final Context context;
    protected final List<T> items;

    protected ItemRecyclerAdapter(Context context, List<T> items) {
        this.context = context;
        this.items = items;
    }

    abstract int obtainLayout(int viewType);

    abstract RecyclerItemHolder<T> obtainHolder(int viewType, View view);

    protected View inflateItemView(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return inflater.inflate(obtainLayout(viewType), parent, false);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerItemHolder<T> holder, int position) {
        final T item = items.get(position);
        holder.bind(item);
    }

    @NonNull
    @Override
    public RecyclerItemHolder<T> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = inflateItemView(parent, viewType);
        return obtainHolder(viewType, view);
    }
}
