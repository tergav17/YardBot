package com.tergav17.bot;

public class CasualPhraseProcessor {
    //Phrases that make the bot have a real one

    public static boolean botAddressed(String in) {
        if (in.startsWith("yardbot") || in.startsWith("yard bot")) return true;
        if (in.startsWith("hey yardbot") || in.startsWith("hey yard bot")) return true;
        if (in.startsWith("eyo yardbot") || in.startsWith("eyo yard bot")) return true;
        if (in.startsWith("yo yardbot") || in.startsWith("yo yard bot")) return true;

        return false;
    }

    public static boolean botJoinCallCommand(String in) {
        if (in.contains("join")) return true;
        if (in.contains("get in")) return true;
        if (in.contains("enter")) return true;

        return false;
    }

    public static boolean botLeaveCallCommand(String in) {
        if (in.contains("leave")) return true;
        if (in.contains("get out")) return true;
        if (in.contains("exit")) return true;

        return false;
    }
}
