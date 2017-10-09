package com.github.jotask.gametracker.igdb;

import android.content.Context;
import com.android.volley.VolleyError;
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

    protected static final String KEY = "1adb0a2cdf042857fc6f6cc7c4a8971f";

    public enum DATA{
        NAME, COVER
    }

    private ArrayList<DataModel> dataModels;

    public ApiSearch() {
        this.dataModels = new ArrayList<>();
    }

    private DataModel jsonToDataModel(JSONObject obj) throws JSONException {
        String name = obj.getString(DATA.NAME.name().toLowerCase());
        String cover = obj.getJSONObject(DATA.COVER.name().toLowerCase()).getString("url");
        cover = cover.substring(2);
        return new DataModel(name, cover);
    }


    public void searchGame(final Context context, final String name) {

        dataModels.clear();

        System.out.println("-------------------------------------------------" + "Searching: " + name);

//        adapter.restart();
//        adapter.notifyDataSetChanged();

        APIWrapper wrapper = new APIWrapper(context, KEY);

        Parameters params = new Parameters()
                .addSearch(name)
                .addFields("name,cover");

        wrapper.games(params, new onSuccessCallback(){
            @Override
            public void onSuccess(JSONArray result) {
//                System.out.println("---------------------------------------" + result.length());
                for(int i = 0; i < result.length(); i++) {
                    JSONObject obj;
                    try {
                        obj = result.getJSONObject(i);
                    } catch (JSONException e) {
//                        System.err.println("--------------------------- one");
                        continue;
                    }
                    DataModel model;
                    try {
                        model = jsonToDataModel(obj);
                    } catch (JSONException e) {
//                        System.err.println("--------------------------- two");
                        continue;
                    }
                    dataModels.add(model);
                }
//                listView.invalidateViews();
            }

            @Override
            public void onError(VolleyError error) {
                // Do something on error
            }

        });

//        listView.setAdapter(adapter);
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


//        adapter.notifyDataSetChanged();

    }

    public ArrayList<DataModel> getDataModels() { return dataModels; }

}