package com.github.jotask.gametracker.model;

import java.util.ArrayList;

/**
 * Game
 *
 * @author Jose Vives Iznardo
 * @since 10/10/2017
 */
public class Game {

    public String id;

    public boolean completed = false;

    public long start = -1;
    public long end = -1;

    public ArrayList<User> playedWith = new ArrayList<>();

    public Game() { }

}
