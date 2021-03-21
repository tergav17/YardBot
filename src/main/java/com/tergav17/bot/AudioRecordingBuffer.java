package com.tergav17.bot;

import java.util.ArrayList;

public class AudioRecordingBuffer {
    private static final int frameSize = 3840;

    private ArrayList<StableAudioClip> buffer = new ArrayList<>();

    private boolean isRecording;
    private long recordingStart;

    public AudioRecordingBuffer() {
        isRecording = false;
    }

    public void startRecording() {
        isRecording = true;
        recordingStart = System.currentTimeMillis();
    }

    public long getRecordingStart() {
        return recordingStart;
    }

    public void dumpBuffer() {
        buffer.removeAll(buffer);
    }

    public void clean() {
        if (!isRecording) {
            while (buffer.size() > 2000) buffer.remove(0);
        }

        while (buffer.size() > 60000) buffer.remove(0);
    }

    public void stopRecording() {
        isRecording = false;
    }

    public boolean isRecording() {
        return isRecording;
    }

    public void add(StableAudioClip c) {
        buffer.add(c);
    }

    public byte[] clip(int startTime, int offset) {

        if (startTime < 0) startTime = 1;

        byte clip[] = new byte[startTime * frameSize];

        int address = buffer.size() - startTime;
        address = address - offset;

        if (address < 0) address = 0;

        int clipAddress = 0;

        for (; address < buffer.size()-1; address++) {
            for (int i = 0; i < frameSize; i++) {
                clip[(clipAddress * frameSize) + i] = buffer.get(address).getContent()[i];
            }

            clipAddress++;

            if (clipAddress >= startTime) break;
        }

        return clip;
    }
}
