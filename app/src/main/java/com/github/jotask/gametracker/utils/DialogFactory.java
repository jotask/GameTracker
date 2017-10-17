package com.github.jotask.gametracker.utils;

import android.app.Dialog;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.*;
import com.github.jotask.gametracker.MainActivity;
import com.github.jotask.gametracker.R;
import com.github.jotask.gametracker.model.Game;
import com.github.jotask.gametracker.model.User;
import com.github.jotask.gametracker.utils.adapters.UsersAdapter;

import java.util.ArrayList;

/**
 * DialogFactory
 *
 * @author Jose Vives Iznardo
 * @since 15/10/2017
 */
public class DialogFactory {

    public static void addUser(final MainActivity main, final Game game){

        final Dialog dialog = new Dialog(main);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_add_user);

        final Button dialogButton = dialog.findViewById(R.id.dialog_adduser_add_btn);
        final Button dialogSearchUserBtm = dialog.findViewById(R.id.dialog_add_user_searchUser_btn);
        final EditText editUser = dialog.findViewById(R.id.dialog_adduser_searchuser);
        final ListViewMaxHeight list = dialog.findViewById(R.id.dialog_adduser_listview);
        final LinearLayout userSelected = dialog.findViewById(R.id.dialog_adduser_userselected);

        dialogButton.setText("Exit");

        final UsersAdapter adapter = new UsersAdapter(main, new ArrayList<User>());

        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                View inflatedView = View.inflate(dialog.getContext(), R.layout.row_user, userSelected);

                final TextView name = inflatedView.findViewById(R.id.item_name);
                final ImageView cover = inflatedView.findViewById(R.id.item_cover);
                final ImageView info = inflatedView.findViewById(R.id.item_info);

                info.setVisibility(View.INVISIBLE);

                final User u = adapter.getItem(position);

                name.setText(u.name);
                new LoadImage(cover, "https://" + u.photo).execute();

                dialogButton.setText("Add user");

                dialogButton.setTag(u);

                list.setVisibility(View.GONE);

            }
        });

        final Handler handler  = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 0:
                        main.getFirebase().getAllFriends(this);
                        System.out.println("***************************************** MSG 0");
                        break;
                    case 1:
                        System.out.println("***************************************** MSG 1");
                        break;
                    case 2:
                        final ArrayList<User> friends = (ArrayList<User>) msg.obj;
                        adapter.clearData();
                        adapter.addData(friends);
                        System.out.println("***************************************** MSG 2");
                        break;
                }
            }
        };

        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                other: if(dialogButton.getTag() instanceof User){
                    final User u = (User) dialogButton.getTag();

                    for(User tmp: game.playedWith){
                        if(tmp.uid.equals(u.uid))
                            break other;
                    }

                    main.getFirebase().gamePlayedWith(game, u);
                }

                dialog.cancel();
            }
        });

        handler.sendEmptyMessage(0);

        dialog.show();

    }

}
