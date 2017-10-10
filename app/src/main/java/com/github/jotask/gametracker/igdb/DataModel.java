package com.github.jotask.gametracker.igdb;

/**
 * DataModel
 *
 * @author Jose Vives Iznardo
 * @since 08/10/2017
 */
public class DataModel {

    String id;
    String name;
    String cover_url;

    public DataModel(String id,String name, String cover_url) {
        this.id = id;
        this.name = name;
        this.cover_url = cover_url;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getCover_url() { return cover_url; }

}
