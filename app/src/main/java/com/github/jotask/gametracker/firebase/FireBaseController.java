package com.github.jotask.gametracker.firebase;

import android.os.Handler;
import android.os.Message;
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

    public void getAllUsers(final Handler handler) {

        final DatabaseReference ref = this.database.getReference( );
        ref.child(TABLES.USERS.name().toLowerCase())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(!dataSnapshot.exists())
                            return;

                        ArrayList<User> result = new ArrayList<>();

                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                            User u = postSnapshot.getValue(User.class);

                            result.add(u);

                        }

                        // Send the result to handle
                        Message msg = handler.obtainMessage(1, result);
                        handler.sendMessage(msg);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) { }

                });

    }

    public void getAllFriends(final Handler handler) {

        final DatabaseReference ref = this.database.getReference( );
        ref.child(TABLES.FRIENDS.name().toLowerCase())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(!dataSnapshot.exists()) {
                            return;
                        }

                        ArrayList<User> result = new ArrayList<>();

                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            User u = postSnapshot.getValue(User.class);
                            result.add(u);
                        }

                        // Send the result to handle
                        Message msg = handler.obtainMessage(2, result);
                        handler.sendMessage(msg);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) { }

                });

    }

    public void getGameData(final String gameId, final Handler handler) {

        final DatabaseReference ref = this.database.getReference( ).
                child(TABLES.GAMES.name().toLowerCase()).child(user.getUid()).child(gameId);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    // TODO add error handler

                    final Game game;

                    if(!dataSnapshot.exists()){
                        game = new Game();
                        game.id = gameId;

                        ref.setValue(game);

                        System.out.println("********** created because doesm't exist");

                    }else{

//                            if(!(dataSnapshot.getValue() instanceof Game)){
//                                throw new RuntimeException("Is not instance of game: instance of: " + dataSnapshot.getValue().getClass().getSimpleName());
//                            }

                        game = dataSnapshot.getValue(Game.class);

                    }

                    // Send the result to handle
                    Message msg = handler.obtainMessage(1, game);
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

    public void subscribeToUser(final User user) {

        final DatabaseReference ref = this.database.getReference();
        ref.child(TABLES.FRIENDS.name().toLowerCase())
                .child(this.user.getUid())
                .child(user.uid).setValue(user);

    }


    public void updateGameUser(final Game gameUser) {
        final DatabaseReference ref = this.database.getReference()
                .child(TABLES.GAMES.name().toLowerCase())
                .child(user.getUid())
                .child(gameUser.id);
        ref.setValue(gameUser);
    }


}