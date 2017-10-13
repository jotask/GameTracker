package com.github.jotask.gametracker.igdb;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.android.volley.VolleyError;
import com.github.jotask.gametracker.sections.GameProfile;
import com.igdb.api_android_java.callback.onSuccessCallback;
import com.igdb.api_android_java.model.APIWrapper;
import com.igdb.api_android_java.model.Parameters;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * ApiSearch
 *
 * @author Jose Vives Iznardo
 * @since 09/10/2017
 */
public class ApiSearch {

    private static final String KEY = "fc0505f1eceb630981d04505bcd884f0";

    public enum DATA { ID, NAME, COVER, DSC }

    final APIWrapper wrapper;

    final Context ctx;

    public ApiSearch(final Context context) {
        this.wrapper = new APIWrapper(context, KEY);
        this.ctx = context;
    }

    private DataModel jsonToDataModel(JSONObject obj) throws JSONException {
        String id = obj.getString(DATA.ID.name().toLowerCase());
        String name = obj.getString(DATA.NAME.name().toLowerCase());
        String cover = obj.getJSONObject(DATA.COVER.name().toLowerCase()).getString("url");
        cover = cover.substring(2);
        return new DataModel(id, name, cover);
    }

    public void searchGames(final String name, final Handler handler) {

        final ArrayList<DataModel> dataModels = new ArrayList<>();

        Parameters params = new Parameters()
                .addSearch(name)
                .addFields("id, name,cover");

        wrapper.games(params, new onSuccessCallback(){
            @Override
            public void onSuccess(JSONArray result) {

                for(int i = 0; i < result.length(); i++) {
                    JSONObject obj;
                    try {
                        obj = result.getJSONObject(i);
                    } catch (JSONException e) { continue; }
                    DataModel model;
                    try {
                        model = jsonToDataModel(obj);
                    } catch (JSONException e) { continue; }
                    dataModels.add(model);

                    // Send the result to handle
                    Message msg = handler.obtainMessage(1, dataModels);
                    handler.sendMessage(msg);

                }
            }

            @Override
            public void onError(VolleyError error) {  /* Do something on error*/ }

        });

    }

    public void searchGamesbyId(ArrayList<String> games, final Handler handler) {

        final ArrayList<DataModel> dataModels = new ArrayList<>();

        final StringBuilder sb = new StringBuilder();

        for(final String tmp: games)
            sb.append(tmp + ",");

        final String tmp = sb.toString();

        final String query = tmp.substring(0, tmp.length() - 1);

        Parameters params = new Parameters()
                .addIds(query)
                .addFields("id, name, cover");

        wrapper.games(params, new onSuccessCallback(){
            @Override
            public void onSuccess(JSONArray result) {

                System.out.println(result.toString());

                for(int i = 0; i < result.length(); i++) {

                    JSONObject obj = null;
                    try {
                        obj = result.getJSONObject(i);
                    } catch (JSONException e) { continue; }
                    DataModel model;
                    try {
                        model = jsonToDataModel(obj);
                    } catch (JSONException e) { continue; }
                    dataModels.add(model);

                    // Send the result to handle
                    Message msg = handler.obtainMessage(1, dataModels);
                    handler.sendMessage(msg);

                }
            }

            @Override
            public void onError(VolleyError error) {  /* Do something on error*/ }

        });

    }

    public void getGameData(final GameProfile profile){

        Parameters params = new Parameters()
                .addIds(profile.getGameID());


        wrapper.games(params, new onSuccessCallback(){
            @Override
            public void onSuccess(JSONArray obj) {
                System.out.println(obj.toString());

                save(obj.toString());

                JSONObject data = null;

                try { data = obj.getJSONObject(0);
                } catch (JSONException e) { e.printStackTrace(); }

                try {

                    FullGame fg = new FullGame(data);
                    profile.setProfile(fg);

                    // Send the result to handle
//                    Message msg = handler.obtainMessage(1, dataModels);
//                    handler.sendMessage(msg);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(VolleyError error) { /* Do something on error */ }

        });

    }

    public void getGameUserData(final String id, final Handler handler){

        Parameters params = new Parameters().addIds(id);

        wrapper.games(params, new onSuccessCallback(){
            @Override
            public void onSuccess(JSONArray obj) {

                save(obj.toString());

                JSONObject data = null;

                try { data = obj.getJSONObject(0);
                } catch (JSONException e) { e.printStackTrace(); }

                try {

                    FullGame fg = new FullGame(data);

                    // Send the result to handle
                    Message msg = handler.obtainMessage(2, fg);
                    handler.sendMessage(msg);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(VolleyError error) { /* Do something on error */ }

        });

    }

    /** Method to check whether external media available and writable. This is adapted from
     http://developer.android.com/guide/topics/data/data-storage.html#filesExternal */

    private void checkExternalMedia(){
        boolean mExternalStorageAvailable = false;
        boolean mExternalStorageWriteable = false;
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // Can read and write the media
            mExternalStorageAvailable = mExternalStorageWriteable = true;

            System.out.println("*********************************************" + "Can read and write the media");
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            // Can only read the media
            mExternalStorageAvailable = true;
            mExternalStorageWriteable = false;
            System.out.println("*********************************************" + "Can only read the media");
        } else {
            // Can't read or write
            mExternalStorageAvailable = mExternalStorageWriteable = false;
            System.out.println("*********************************************" + "Can't read or write");
        }
    }

    /** Method to write ascii text characters to file on SD card. Note that you must add a
     WRITE_EXTERNAL_STORAGE permission to the manifest file or this method will throw
     a FileNotFound Exception because you won't have write permission. */

    private void save(String str){

        checkExternalMedia();

        // Create a path where we will place our private file on external
        // storage.
        File file = new File(ctx.getExternalFilesDir(null), "state.txt");

        System.out.println(file.toString());

        try {

            FileOutputStream os = new FileOutputStream(file);
            OutputStreamWriter out = new OutputStreamWriter(os);

            out.write(str);
            out.close();

//            if(hasExternalStoragePrivateFile()) {
//                Log.w("ExternalStorageFileCreation", "File Created");
//            } else {
//                Log.w("ExternalStorageFileCreation", "File Not Created");
//            }

        } catch (IOException e) {
            // Unable to create file, likely because external storage is
            // not currently mounted.
            Log.w("ExternalStorage", "Error writing " + file, e);
        }

    }

}