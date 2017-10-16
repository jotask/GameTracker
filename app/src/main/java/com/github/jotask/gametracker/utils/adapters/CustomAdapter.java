package com.github.jotask.gametracker.utils.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * CustomAdapter
 *
 * @author Jose Vives Iznardo
 * @since 10/10/2017
 */
public abstract class CustomAdapter<T> extends BaseAdapter {

    protected final Context context;
    protected final List<T> models;

    public CustomAdapter(final Context context, final List<T> models) {
        this.context = context;
        this.models = models;
    }

    public void addData(final ArrayList<T> data) {
        this.models.addAll(data);
        // Notify ui
        this.notifyDataSetChanged();
    }

    public void clearData() {
        this.models.clear();
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return this.models.size();
    }

    @Override
    public T getItem(int position) {
        return this.models.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public abstract View getView(int position, View convertView, ViewGroup parent);

}
