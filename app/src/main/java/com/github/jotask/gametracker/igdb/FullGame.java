package com.github.jotask.gametracker.igdb;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * FullGame
 *
 * @author Jose Vives Iznardo
 * @since 12/10/2017
 */
public class FullGame {

    public enum DATA { ID, NAME, COVER, SUMMARY }

    public enum SIZE { COVER_SMALL, SCREENSHOT_MED, COVER_BIG, LOGO_MED, SCREENSHOT_BIG, SCREENSHOT_HUGE, THUMB, MICRO }

    public String id = "";

    public String name = "";
    private String cover = "";
    public String summary = "";

    private final String coverURLtemplate = "images.igdb.com/igdb/image/upload/t_{size}/{hash}.jpg";

    public FullGame(JSONObject data) throws JSONException {

        this.id = data.getString(DATA.ID.name().toLowerCase());
        this.name = data.getString(DATA.NAME.name().toLowerCase());
        this.cover = data.getJSONObject(DATA.COVER.name().toLowerCase()).getString("cloudinary_id");

        this.summary = data.getString(DATA.SUMMARY.name().toLowerCase());

    }

    public String getCover(){ return getCover(SIZE.THUMB); }

    public String getCover(SIZE size){
        String tmp = new String(coverURLtemplate);
        tmp = tmp.replace("{size}", size.name().toLowerCase());
        tmp = tmp.replace("{hash}", this.cover);
        return tmp;
    }

}
