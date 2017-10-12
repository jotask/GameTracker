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

                        ArrayList<String> result = new ArrayList<>();

                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                            Game g = postSnapshot.getValue(Game.class);
                            result.add(g.id);

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

        // OK

        final DatabaseReference ref = this.database.getReference( );
        ref.child(TABLES.USERS.name().toLowerCase())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(!dataSnapshot.exists())
                            return;

                        ArrayList<DataModel> result = new ArrayList<>();

                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                            User u = postSnapshot.getValue(User.class);

                            DataModel dm = new DataModel(u.uid, u.name, u.photo);

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
        // Ok
        final Game game = new Game();
        game.id = uid_game;
        final DatabaseReference ref = this.database.getReference();
        ref.child(TABLES.GAMES.name().toLowerCase())
                .child(this.user.getUid())
                .child(game.id).setValue(game);
    }

}