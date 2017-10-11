package com.github.jotask.gametracker.model;

/**
 * Game
 *
 * @author Jose Vives Iznardo
 * @since 10/10/2017
 */
public class Game {

    public final String id;

    public boolean completed = false;

    public Game(String id) {
        this.id = id;
    }
}
