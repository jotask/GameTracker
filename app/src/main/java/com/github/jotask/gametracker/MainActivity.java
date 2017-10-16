package com.github.jotask.gametracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.github.jotask.gametracker.firebase.FireBaseController;
import com.github.jotask.gametracker.igdb.ApiSearch;
import com.github.jotask.gametracker.settings.SettingsActivity;
import com.github.jotask.gametracker.sections.ExploreGames;
import com.github.jotask.gametracker.sections.Friends;
import com.github.jotask.gametracker.sections.GameProfile;
import com.github.jotask.gametracker.sections.Library;
import com.github.jotask.gametracker.utils.LoadImage;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth auth;

    private Toolbar toolbar;
    private NavigationView navigationView;

    private ApiSearch api;
    private FireBaseController firebase;
    private FirebaseUser user;

    final int initial = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseApp.initializeApp(this);

        this.auth = FirebaseAuth.getInstance();

        if(this.auth.getCurrentUser() == null) {
            // Not logged in, launch the Log In activity
            // This prevents the user going back to the main activity when they press the Back button from the login view.
            Intent intent = new Intent(this, LogInActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return;
        }

        setContentView(R.layout.activity_main);

        this.toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(this.toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, this.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        this.navigationView = findViewById(R.id.nav_view);
        this.navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);

        this.user = this.auth.getCurrentUser();

        TextView name = header.findViewById(R.id.profile_name);
        name.setText(this.user.getDisplayName());
        TextView mail = header.findViewById(R.id.profile_mail);
        mail.setText(this.user.getEmail());
        ImageView img = header.findViewById(R.id.profile_img);
        new LoadImage(img, this.user.getPhotoUrl().toString()).execute();

        this.api = new ApiSearch(this);
        this.firebase = new FireBaseController();

        onNavigationItemSelected(this.navigationView.getMenu().getItem(initial));

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
        switch(id){
            case R.id.action_settings:
                System.out.println("----------------------------------------------------2 ");
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.action_logout:
                auth.signOut();
                Intent intent = new Intent(this, LogInActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fragment = null;

        switch (id){
            case R.id.nav_library:
                fragment = new Library();
                break;
            case R.id.nav_friends:
                fragment = new Friends();
                break;
            case R.id.nav_explore:
                fragment = new ExploreGames();
                break;
        }

        if(fragment == null){
            return false;
        }

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_main, fragment);
        ft.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    void setTest(){
        String gameID = "1074";
        Fragment fragment = GameProfile.newInstance(gameID);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_main, fragment);
        ft.commit();

    }

    public ApiSearch getApi() { return api; }
    public FireBaseController getFirebase() { return firebase; }

}
