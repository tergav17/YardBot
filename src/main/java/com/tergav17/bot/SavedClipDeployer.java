package com.tergav17.bot;

import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.io.*;
import java.util.List;

public class SavedClipDeployer {
    public static void deployClip(Guild g) {

        List<TextChannel> channels = g.getTextChannels();

        Message message = new MessageBuilder().append("Clip attached: ").build();

        File f = new File("etc/clip.wav");

        if (!f.exists() || !f.isFile()) return;

        for (TextChannel chan : channels) {
            if (chan.getName().startsWith("no-mic")) {

                if (f.length() < 8000000) {
                    chan.sendFile(f, "clip.wav").queue();
                } else {
                    System.out.println("File too big!");

                    String cmd = "etc\\util\\ffmpeg.exe -i etc\\clip.wav -acodec libmp3lame etc\\output.mp3 -y";
                    callFFMPEG(cmd);

                    f = new File("etc/output.mp3");

                    if (f.length() < 8000000) {
                        chan.sendFile(f, "clip.mp3").queue();
                    } else {
                        System.out.println("File still too big!");

                        cmd = "etc\\util\\ffmpeg.exe -i etc\\clip.wav -codec:a aac -q:a 0 etc\\output.aac -y";
                        callFFMPEG(cmd);

                        f = new File("etc/output.aac");

                        chan.sendFile(f, "clip.mp3").queue();

                    }

                }
            }
        }
    }

    private static void callFFMPEG(String cmd) {
        System.out.println("Calling FFMPEG...");

        try {
            Process proc = Runtime.getRuntime().exec(cmd);

            BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line = "";
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Completed");
    }
}
