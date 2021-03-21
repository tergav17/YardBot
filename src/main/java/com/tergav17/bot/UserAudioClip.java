package com.tergav17.bot;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

public class UserAudioClip {
    private long creationTime;
    private long lastUpdate;

    private Queue<byte[]> buffer = new ConcurrentLinkedDeque<>();

    public UserAudioClip() {
        creationTime = System.currentTimeMillis();
        lastUpdate = creationTime;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(long creationTime) {
        this.creationTime = creationTime;
    }

    public long getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Queue<byte[]> getBuffer() {
        return buffer;
    }

    public void setBuffer(Queue<byte[]> buffer) {
        this.buffer = buffer;
    }
}
