package com.tergav17.bot;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.playback.MutableAudioFrame;
import net.dv8tion.jda.api.audio.*;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.DataInputStream;
import java.io.File;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class VoiceChannelHandler implements AudioSendHandler, AudioReceiveHandler {

    private HashMap<String, UserAudioClip> audioBuffer = new HashMap<>();
    private VoiceConnectionHandler vch;

    private AudioPlayer audioPlayer;
    private ByteBuffer buffer;
    private MutableAudioFrame frame;


    public VoiceChannelHandler(VoiceConnectionHandler vch, AudioPlayer player) {
        this.vch = vch;
        this.audioPlayer = player;
        this.buffer = ByteBuffer.allocate(1024);
        this.frame = new MutableAudioFrame();
        this.frame.setBuffer(buffer);

    }

    @Override
    public boolean canProvide() {
        return audioPlayer.provide(frame);
    }

    @Nullable
    @Override
    public ByteBuffer provide20MsAudio() {
        ((Buffer) buffer).flip();

        return buffer;
    }

    @Override
    public boolean isOpus() {
        return true;
    }

    @Override
    public boolean canReceiveCombined() {

        vch.arb.clean();

        ArrayList<String> clipsToProcess = new ArrayList<>();

        for (String key : audioBuffer.keySet()) {
            long sTime = System.currentTimeMillis();
            UserAudioClip clip = audioBuffer.get(key);

            if (clip.getLastUpdate() + 100 < sTime) {
                System.out.println("Timeout: " + clip.getLastUpdate() + ", " + sTime);
                clipsToProcess.add(key);
            }
        }

        for (String key : clipsToProcess) {
            System.out.println("Ending clip " + key);
            UserAudioClip clip = audioBuffer.get(key);
            audioBuffer.remove(key);

            byte[] clipBinary = AudioConversionUtils.convertToByteArray(clip);

            (new SpeechCommandDispatch(clipBinary, this, vch, clip.getCreationTime())).run();
        }

        return true;
    }

    @Override
    public boolean canReceiveUser() {
        return true;
    }

    @Override
    public void handleCombinedAudio(@NotNull CombinedAudio combinedAudio) {
        String user = "all";

        vch.arb.add(new StableAudioClip(combinedAudio.getAudioData(1.0F), System.currentTimeMillis()));
        /*
        if (audioBuffer.containsKey(user)) {
            UserAudioClip clip = audioBuffer.get(user);

            if (!combinedAudio.getUsers().isEmpty()) clip.setLastUpdate(System.currentTimeMillis());
            clip.getBuffer().add(combinedAudio.getAudioData(0.2F));

        } else if (!combinedAudio.getUsers().isEmpty()) {
            System.out.println("Starting clip for " + user);
            audioBuffer.put(user, new UserAudioClip());
        } */

    }

    @Override
    public void handleUserAudio(@NotNull UserAudio userAudio) {

        String user = userAudio.getUser().getAsTag();

        if (audioBuffer.containsKey(user)) {
            UserAudioClip clip = audioBuffer.get(user);

            clip.setLastUpdate(System.currentTimeMillis());
            clip.getBuffer().add(userAudio.getAudioData(0.2F));

        } else {
            System.out.println("Starting clip for " + userAudio.getUser().getAsTag());
            audioBuffer.put(user, new UserAudioClip());
        }


    }

    @Override
    public boolean includeUserInCombinedAudio(@NotNull User user) {
        return true;
    }


}
