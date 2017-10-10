package com.github.jotask.gametracker.utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.github.jotask.gametracker.R;
import com.github.jotask.gametracker.igdb.DataModel;

import java.util.ArrayList;
import java.util.List;

/**
 * CustomAdapter
 *
 * @author Jose Vives Iznardo
 * @since 10/10/2017
 */
public class CustomAdapter extends BaseAdapter {

    private final Context context;
    private final List<DataModel> models;

    public CustomAdapter(final Context context, final List<DataModel> models) {
        this.context = context;
        this.models = models;
    }

    public void addData(final ArrayList<DataModel> data) {
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
    public Object getItem(int position) {
        return this.models.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = View.inflate(this.context, R.layout.row_item, null);
        TextView name = v.findViewById(R.id.game_profile_name);
        ImageView cover = v.findViewById(R.id.game_profile_cover);

        final DataModel model = this.models.get(position);

        name.setText(model.getName());
        new LoadImage(cover, "https://" + model.getCover_url()).execute();

        v.setTag(this.models.get(position).getId());

        return v;
    }

}
