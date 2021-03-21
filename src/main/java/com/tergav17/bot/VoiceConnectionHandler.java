package com.tergav17.bot;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

public class VoiceConnectionHandler {

    private String name;
    private boolean isConnected;

    public SpeechRecognitionHandler sph = new SpeechRecognitionHandler();
    public AudioRecordingBuffer arb = new AudioRecordingBuffer();

    public AudioPlayerManager apm;
    public AudioPlayer player;

    public Guild guild;

    public VoiceConnectionHandler(Guild g, AudioPlayerManager apm) {
        this.apm = apm;
        player = apm.createPlayer();

        name = g.getId();
        isConnected = false;
        guild = g;
    }

    public String getName() {
        return name;
    }

    //Join a voice channel
    public void joinCommandHandler(GuildMessageReceivedEvent event) {
        Member member = event.getMember();
        GuildVoiceState voiceState = member.getVoiceState();            // Check the current voice state of the user
        VoiceChannel channel = voiceState.getChannel();                 // Use the channel the user is currently connected to
        if (channel != null) {
            connectTo(channel);                                         // Join the channel of the user
            printChannel(event.getChannel(), "Joining voice channel...");
        }
        else {
            printChannel(event.getChannel(), "There is no channel to join");
        }
    }

    //Connect to a voice channel
    private void connectTo(VoiceChannel channel) {
        //Set up audio manager
        Guild guild = channel.getGuild();

        //Disconnect
        if (isConnected) disconnect(guild);

        AudioManager audioManager = guild.getAudioManager();
        VoiceChannelHandler handler = new VoiceChannelHandler(this, player);

        //Set handlers
        audioManager.setSendingHandler(handler);
        audioManager.setReceivingHandler(handler);
        audioManager.openAudioConnection(channel);

        isConnected = true;

        arb.dumpBuffer();
    }

    public void disconnect(Guild guild) {
        AudioManager audioManager = guild.getAudioManager();

        audioManager.closeAudioConnection();

        isConnected = false;
    }

    //Prints out to a channel
    private void printChannel(MessageChannel channel, String message) {
        channel.sendMessage(message).queue();
    }

    //Plays from an audio source
    public void playAudio(String url) {
        apm.loadItem(url, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                player.startTrack(track, false);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                AudioTrack track = playlist.getSelectedTrack();

                if (track == null) track = playlist.getTracks().get(0);

                player.startTrack(track, false);
            }

            @Override
            public void noMatches() {
                System.out.println("Could not load resource, not found!");
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                System.out.println("Could not load resource, failure occurred!");
            }
        });
    }
}
