package com.github.jotask.gametracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * LoggedActivity
 *
 * @author Jose Vives Iznardo
 * @since 08/10/2017
 */
public class LoggedActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseUser user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseApp.initializeApp(this);

        this.auth = FirebaseAuth.getInstance();
        this.user = auth.getCurrentUser();

        if(this.user == null)
        {
            // Not logged in, launch the Log In activity
            // This prevents the user going back to the main activity when they press the Back button from the login view.
            Intent intent = new Intent(this, LogInActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

    }
}
