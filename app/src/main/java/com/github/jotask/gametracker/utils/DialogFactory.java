package com.github.jotask.gametracker.utils;

import android.app.Dialog;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import com.github.jotask.gametracker.MainActivity;
import com.github.jotask.gametracker.R;
import com.github.jotask.gametracker.model.User;
import com.github.jotask.gametracker.utils.adapters.CustomAdapter;
import com.github.jotask.gametracker.utils.adapters.UsersAdapter;

import java.util.ArrayList;

/**
 * DialogFactory
 *
 * @author Jose Vives Iznardo
 * @since 15/10/2017
 */
public class DialogFactory {

    public static void addUser(final MainActivity main){

        final Dialog dialog = new Dialog(main);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_add_user);

        final Button dialogButton = dialog.findViewById(R.id.dialog_adduser_add_btn);
        final Button dialogSearchUserBtm = dialog.findViewById(R.id.dialog_add_user_searchUser_btn);
        final EditText editUser = dialog.findViewById(R.id.dialog_adduser_searchuser);
        final ListViewMaxHeight list = dialog.findViewById(R.id.dialog_adduser_listview);

        final CustomAdapter adapter = new UsersAdapter(main, new ArrayList<User>());

        list.setAdapter(adapter);

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
                        ArrayList<User> friends = (ArrayList<User>) msg.obj;
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
                dialog.cancel();
            }
        });

        handler.sendEmptyMessage(0);

        dialog.show();

    }

}
