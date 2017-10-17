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
import java.util.HashMap;
import java.util.List;

/**
 * FriendsAdapter
 *
 * @author Jose Vives Iznardo
 * @since 16/10/2017
 */
public class FriendsAdapter extends CustomAdapter<User> {

    private HashMap<String, Boolean> map;

    public FriendsAdapter(Context context, List<User> models) {
        super(context, models);
        this.map = new HashMap<>();
    }

    @Override
    public void addData(ArrayList<User> data) {
        super.addData(data);
        for(final User u: data){
            this.map.put(u.uid, false);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = View.inflate(this.context, R.layout.row_user, null);

        TextView name = v.findViewById(R.id.item_name);
        ImageView cover = v.findViewById(R.id.item_cover);
        ImageView state = v.findViewById(R.id.item_info);

        final User user = this.models.get(position);

        if(this.map.containsKey(user.uid)){
            state.setImageResource(R.drawable.ic_menu_send);
        }

        name.setText(user.name);

        new LoadImage(cover, "https://" + user.photo).execute();

        v.setTag(user);

        return v;

    }

    public void isFriend(final User friend){
        map.put(friend.uid, true);
    }

}
