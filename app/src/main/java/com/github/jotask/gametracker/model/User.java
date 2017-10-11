package com.github.jotask.gametracker.model;

/**
 * User
 *
 * @author Jose Vives Iznardo
 * @since 11/10/2017
 */
public class User {

    public String uid;
    public String name;
    public String photo;

    public User(String uid, String name, String photo) {
        this.uid = uid;
        this.name = name;
        this.photo = photo;
    }
}
