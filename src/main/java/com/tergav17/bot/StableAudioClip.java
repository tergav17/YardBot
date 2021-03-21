package com.tergav17.bot;

public class StableAudioClip {
    private byte[] content;
    private long creationTime;

    public StableAudioClip(byte[] content, long creationTime) {
        this.content = content;
        this.creationTime = creationTime;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(long creationTime) {
        this.creationTime = creationTime;
    }
}
