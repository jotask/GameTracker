package com.github.jotask.gametracker.igdb;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import com.android.volley.VolleyError;
import com.github.jotask.gametracker.sections.GameProfile;
import com.igdb.api_android_java.callback.onSuccessCallback;
import com.igdb.api_android_java.model.APIWrapper;
import com.igdb.api_android_java.model.Parameters;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * ApiSearch
 *
 * @author Jose Vives Iznardo
 * @since 09/10/2017
 */
public class ApiSearch {

    private static final String KEY = "1adb0a2cdf042857fc6f6cc7c4a8971f";

    public enum DATA { ID, NAME, COVER }

    final APIWrapper wrapper;

    public ApiSearch(final Context context) {
        this.wrapper = new APIWrapper(context, KEY);
    }

    private DataModel jsonToDataModel(JSONObject obj) throws JSONException {
        String id = obj.getString(DATA.ID.name().toLowerCase());
        String name = obj.getString(DATA.NAME.name().toLowerCase());
        String cover = obj.getJSONObject(DATA.COVER.name().toLowerCase()).getString("url");
        cover = cover.substring(2);
        return new DataModel(id, name, cover);
    }

    public void searchGame(final String name, final Handler handler) {

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

    public void getGameData(final GameProfile profile){

        Parameters params = new Parameters()
                .addIds(profile.getGameID());

        wrapper.games(params, new onSuccessCallback(){
            @Override
            public void onSuccess(JSONArray obj) {
                System.out.println(obj.toString());

                JSONObject data = null;

                try { data = obj.getJSONObject(0);
                } catch (JSONException e) { e.printStackTrace(); }

                try {
                    String id = data.getString(DATA.ID.name().toLowerCase());
                    String name = data.getString(DATA.NAME.name().toLowerCase());
                    String cover = data.getJSONObject(DATA.COVER.name().toLowerCase()).getString("url").substring(2);

                    profile.setProfile(id, name, cover);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(VolleyError error) { /* Do something on error */ }

        });

    }

}