package com.github.jotask.gametracker.firebase;

import android.os.Handler;
import android.os.Message;
import com.github.jotask.gametracker.igdb.DataModel;
import com.github.jotask.gametracker.model.Game;
import com.github.jotask.gametracker.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * FireBaseController
 *
 * @author Jose Vives Iznardo
 * @since 10/10/2017
 */
public class FireBaseController {

    public enum TABLES { USERS, GAMES, FRIENDS}

    private final FirebaseDatabase database;
    private final FirebaseUser user;

    public FireBaseController() {
        this.database = FirebaseDatabase.getInstance();
        this.user = FirebaseAuth.getInstance().getCurrentUser();
    }

    public void getAllGamesSubscribed(final Handler handler) {

        final DatabaseReference ref = this.database.getReference( );
        ref.child(TABLES.GAMES.name().toLowerCase())
                .child(this.user.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(!dataSnapshot.exists())
                            return;

                        Map<String, Game> td = (HashMap<String,Game>) dataSnapshot.getValue();

                        final ArrayList<String> result = new ArrayList<>();

                        for(String uid: td.keySet()){
                            result.add(uid);
                        }

                        // Send the result to handle
                        Message msg = handler.obtainMessage(2, result);
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) { }

                });

    }

    public void getAllFriends(final Handler handler) {

        /*
        final DatabaseReference ref = this.database.getReference(TABLES.USERS.name().toLowerCase() );
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                final Map<String, HashMap<String, User>> td = (HashMap<String, HashMap<String, User>>) dataSnapshot.getValue();
//
//                final ArrayList<DataModel> result = new ArrayList<>();
//
//                HashMap<String, User> ot = td.get(user.getUid());

                final HashMap<String, User> users = (HashMap<String, User>) dataSnapshot.getValue();

                for(User u: users.values()){
                    System.out.println(u.name);
                }

//
//                HashMap<String, HashMap<String, String>> tmp = (HashMap<String, HashMap<String, String>>) dataSnapshot.getValue();
//
//                for (HashMap<String, String> o: tmp.values()){
//                    String id = o.get("uid");
//                    String na = o.get("name");
//                    String ph = o.get("photo").substring(8);
//                    System.out.println(ph);
//                    DataModel dm = new DataModel(id, na, ph);
//                    result.add(dm);
//                }

                // Send the result to handle
//                Message msg = handler.obtainMessage(1, result);
//                handler.sendMessage(msg);

            }


            public void onCancelled(DatabaseError databaseError) { }

        });

        */


        final DatabaseReference ref = this.database.getReference( );
        ref.child(TABLES.USERS.name().toLowerCase())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(!dataSnapshot.exists())
                            return;

                        ArrayList<DataModel> result = new ArrayList<>();

                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            //Getting the data from snapshot

                            User u = postSnapshot.getValue(User.class);

                            DataModel dm = new DataModel(u.uid, u.name, u.photo);

//                            System.out.println("one: " + dm.cover_url);

//                            dm.cover_url = dm.cover_url.substring(8, dm.cover_url.length() - 1);

//                            System.out.println("two: " + dm.cover_url);

                            result.add(dm);

                        }

                        // Send the result to handle
                        Message msg = handler.obtainMessage(1, result);
                        handler.sendMessage(msg);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) { }

                });

    }

    public void subscribeToGame(final String uid_game) {
        final Game game = new Game();
        game.id = uid_game;
        final DatabaseReference ref = this.database.getReference();
        ref.child(TABLES.GAMES.name().toLowerCase())
                .child(this.user.getUid())
                .child(game.id).setValue(game);
    }

}