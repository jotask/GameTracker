package com.github.jotask.gametracker;

/**
 * DataModel
 *
 * @author Jose Vives Iznardo
 * @since 08/10/2017
 */
public class DataModel {

    String name;
    String cover_url;

    public DataModel(String name, String cover_url) {
        this.name = name;
        this.cover_url = cover_url;
    }

    public String getName() { return name; }
    public String getCover_url() { return cover_url; }

}
