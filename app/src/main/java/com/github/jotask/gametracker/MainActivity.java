package com.github.jotask.gametracker;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.android.volley.VolleyError;
import com.igdb.api_android_java.callback.onSuccessCallback;
import com.igdb.api_android_java.model.APIWrapper;
import com.igdb.api_android_java.model.Parameters;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends LoggedActivity implements NavigationView.OnNavigationItemSelectedListener {

    protected static final String KEY = "1adb0a2cdf042857fc6f6cc7c4a8971f";

    ArrayList<DataModel> dataModels;
    ListView listView;
    private static CustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        /////

        ImageView profile_img = navigationView.getHeaderView(0).findViewById(R.id.profile_img);
        TextView profile_name = navigationView.getHeaderView(0).findViewById(R.id.profile_name);
        TextView profile_mail = navigationView.getHeaderView(0).findViewById(R.id.profile_mail);

//        profile_img.setTag(user.getPhotoUrl());
        new LoadImage(profile_img, user.getPhotoUrl().toString()).execute();
        profile_name.setText(user.getDisplayName());
        profile_mail.setText(user.getEmail());

        /////

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if(s.length() > 3)
//                    searchGame(s.toString());
                listView.setAdapter(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };

        EditText search = findViewById(R.id.search_game);
        search.addTextChangedListener(textWatcher);

        /////////////////////////////////////////////////////////////////////////////////////////////////

        listView = (ListView)findViewById(R.id.list);

        dataModels = new ArrayList<>();
        adapter = new CustomAdapter(dataModels, getApplicationContext());

//        searchGame("zelda");

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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private DataModel jsonToDataModel(JSONObject obj) throws JSONException {
        String name = obj.getString("name");
        String cover = obj.getJSONObject("cover").getString("url");
        cover = cover.substring(2);
        return new DataModel(name, cover);
    }

    private void searchGame(String name) {

        System.out.println("-------------------------------------------------" + "Searching: " + name);

        adapter.restart();
        adapter.notifyDataSetChanged();

        APIWrapper wrapper = new APIWrapper(this, KEY);

        Parameters params = new Parameters()
                .addSearch(name)
                .addFields("name,cover");

        wrapper.games(params, new onSuccessCallback(){
            @Override
            public void onSuccess(JSONArray result) {
                System.out.println("---------------------------------------" + result.length());
                for(int i = 0; i < result.length(); i++) {
                    JSONObject obj;
                    try {
                        obj = result.getJSONObject(i);
                    } catch (JSONException e) {
                        System.err.println("--------------------------- one");
                        continue;
                    }
                    DataModel model;
                    try {
                        model = jsonToDataModel(obj);
                    } catch (JSONException e) {
                        System.err.println("--------------------------- two");
                        continue;
                    }
                    dataModels.add(model);
                }
                listView.invalidateViews();
            }

            @Override
            public void onError(VolleyError error) {
                // Do something on error
            }

        });

        listView.setAdapter(adapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                DataModel dataModel= dataModels.get(position);
//
//                Snackbar.make(view, dataModel.getName(), Snackbar.LENGTH_LONG)
//                        .setAction("No action", null).show();
//            }
//        });


        adapter.notifyDataSetChanged();

    }

}
