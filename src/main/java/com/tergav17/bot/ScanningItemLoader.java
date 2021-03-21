package com.tergav17.bot;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.util.List;

public class ScanningItemLoader {
    private VoiceConnectionHandler vch;
    private List<Message> messages;

    public ScanningItemLoader(VoiceConnectionHandler vch, List<Message> messages) {
        this.vch = vch;
        this.messages = messages;
    }

    public void loadElement(int i) {
        if (i == messages.size()) return;

        String source = "";

        List<MessageEmbed> em = messages.get(i).getEmbeds();
        List<Message.Attachment> at = messages.get(i).getAttachments();


        if (at.size() > 0) {
          Message.Attachment attachment = at.get(0);

          System.out.println("Attachment: " + attachment.getUrl());

          source = attachment.getUrl();
        } else if (em.size() > 0) {
            MessageEmbed embed = em.get(0);

            System.out.println("Embed: " + embed.getUrl());

            source = embed.getUrl();
        } else source = messages.get(i).getContentStripped();

        System.out.println("Attempt " + i + ": " + source);

        vch.apm.loadItem(source, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                vch.player.startTrack(track, false);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                AudioTrack track = playlist.getSelectedTrack();

                if (track == null) track = playlist.getTracks().get(0);

                vch.player.startTrack(track, false);
            }

            @Override
            public void noMatches() {
                failure();
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                failure();
            }

            private void failure() {
                if (i < 100) loadElement(i+1);
            }
        });


    }
}
