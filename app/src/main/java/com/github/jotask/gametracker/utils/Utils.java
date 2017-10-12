package com.github.jotask.gametracker.utils;

/**
 * Utils
 *
 * @author Jose Vives Iznardo
 * @since 12/10/2017
 */
public class Utils {

    public static String removeHTTPS(String str) {
        if(str.startsWith("https://"))
            str = str.replace("https://", "");
        return str;
    }

}
