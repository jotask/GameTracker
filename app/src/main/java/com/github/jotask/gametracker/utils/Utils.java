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

    public enum HandlerStatus{
        START, STOP, API, FIRE
    }

    public static String getDifferenceTime(final long start, final long end){

        long diff = end - start;

        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000) % 24;
        long diffDays = diff / (24 * 60 * 60 * 1000);

        final StringBuilder sb = new StringBuilder();

        sb.append(diffDays + " days, ");
        sb.append(diffHours + " hours, ");
        sb.append(diffMinutes + " minutes, ");
        sb.append(diffSeconds + " seconds.");

        return sb.toString();

    }

}
