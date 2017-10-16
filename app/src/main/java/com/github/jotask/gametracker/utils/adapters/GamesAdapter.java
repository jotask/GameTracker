package com.github.jotask.gametracker.utils.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.github.jotask.gametracker.R;
import com.github.jotask.gametracker.igdb.DataModel;
import com.github.jotask.gametracker.utils.LoadImage;

import java.util.List;

/**
 * GameAdapter
 *
 * @author Jose Vives Iznardo
 * @since 16/10/2017
 */
public class GamesAdapter extends CustomAdapter <DataModel>{

    public GamesAdapter(Context context, List<DataModel> models) {
        super(context, models);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = View.inflate(this.context, R.layout.row_item, null);

        TextView name = v.findViewById(R.id.item_name);
        ImageView cover = v.findViewById(R.id.item_cover);

        final DataModel model = this.models.get(position);

        name.setText(model.getName());

        new LoadImage(cover, "https://" + model.getCover_url()).execute();

        v.setTag(this.models.get(position).getId());

        return v;

    }

}
