package com.github.jotask.gametracker.utils.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.github.jotask.gametracker.R;
import com.github.jotask.gametracker.model.User;
import com.github.jotask.gametracker.utils.LoadImage;

import java.util.ArrayList;
import java.util.List;

/**
 * UsersAdapter
 *
 * @author Jose Vives Iznardo
 * @since 16/10/2017
 */
public class UsersAdapter extends CustomAdapter<User> {

    ArrayList<User> friends;

    public UsersAdapter(Context context, List<User> models) {
        super(context, models);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = View.inflate(this.context, R.layout.row_user, null);

        TextView name = v.findViewById(R.id.item_name);
        ImageView cover = v.findViewById(R.id.item_cover);
        ImageView state = v.findViewById(R.id.item_info);

        final User user = this.models.get(position);

        name.setText(user.name);

        new LoadImage(cover, "https://" + user.photo).execute();

        v.setTag(user);

        return v;

    }

}
