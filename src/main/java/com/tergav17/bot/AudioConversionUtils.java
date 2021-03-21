package com.tergav17.bot;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.*;

public class AudioConversionUtils {
    private static final int frameSize = 3840;

    public static byte[] convertToByteArray(UserAudioClip clip) {
        byte out[] = new byte[clip.getBuffer().size() * 3840];

        int i = 0;
        while (!clip.getBuffer().isEmpty()) {
            byte[] frame = clip.getBuffer().poll();

            for (byte b : frame) {
                out[i] = b;
                i++;
            }
        }

        return out;
    }

    public static void writeDiscordAudioToFile(byte[] input) {
        InputStream b_in = new ByteArrayInputStream(input);
        try {
            AudioFormat format = new AudioFormat(48000f, 16, 2, true, true);
            AudioInputStream stream = new AudioInputStream(b_in, format, input.length);
            File file = new File("etc/clip.wav");
            AudioSystem.write(stream, AudioFileFormat.Type.WAVE, file);
            System.out.println("File saved: " + file.getName() + ", bytes: " + input.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writeStreamAudioToFile(AudioInputStream stream) {
        try {
            File file = new File("etc/clip.wav");
            AudioSystem.write(stream, AudioFileFormat.Type.WAVE, file);
            System.out.println("File saved: " + file.getName() + " as stream");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static AudioInputStream toDiscordAudio(AudioInputStream ais) {
        try {
            AudioFormat newFormat = new AudioFormat(48000f, 16, 2, true, true);

            return AudioSystem.getAudioInputStream(newFormat, ais);

        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }


    public static AudioInputStream toSphinxAudio(byte[] input) {
        InputStream inStream = new ByteArrayInputStream(input);

        try {

            AudioFormat oldFormat = new AudioFormat(48000f, 16, 2, true, true);

            AudioInputStream ais = new AudioInputStream(inStream, oldFormat, input.length);
            AudioFormat newFormat = new AudioFormat(16000f, 16, 1, true, false);

            return AudioSystem.getAudioInputStream(newFormat, ais);

        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }
}
