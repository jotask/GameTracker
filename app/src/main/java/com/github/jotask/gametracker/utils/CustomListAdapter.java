package com.github.jotask.gametracker.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.github.jotask.gametracker.R;
import com.github.jotask.gametracker.igdb.DataModel;

import java.util.ArrayList;

/**
 * CustomAdapter
 *
 * @author Jose Vives Iznardo
 * @since 08/10/2017
 */
public class CustomListAdapter extends BaseAdapter {

    private ArrayList<DataModel> listData;

    private LayoutInflater layoutInflater;

    public CustomListAdapter(Context context, ArrayList<DataModel> _data) {
        this.listData = _data;
        layoutInflater = LayoutInflater.from(context);
    }

    public void setListData(ArrayList<DataModel> data){
        listData = data;
    }

    @Override
    public int getCount() {
        return listData.size();
    }


    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.row_item, null);
            holder = new ViewHolder();
            holder.name = convertView.findViewById(R.id.game_profile_name);
            holder.img = convertView.findViewById(R.id.game_profile_cover);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final DataModel tmp = listData.get(position);

        holder.name.setText(tmp.getName().toString());
//        holder.img.setText(tmp.getUnitArray().get(position).toString());

        return convertView;
    }

    static class ViewHolder {
        TextView name;
        ImageView img;
    }

}