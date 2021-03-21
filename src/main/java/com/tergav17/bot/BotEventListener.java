package com.tergav17.bot;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.ArrayList;
import java.util.Random;

public class BotEventListener extends ListenerAdapter {

    Random r = new Random();

    ArrayList<VoiceConnectionHandler> vchList = new ArrayList<VoiceConnectionHandler>();
    AudioPlayerManager apm;

    public BotEventListener() {
        this.apm = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(apm);
        AudioSourceManagers.registerLocalSource(apm);

        
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        //Ensure that the message does not come from a bot
        if (event.getAuthor().isBot()) return;

        //Grab the message
        Message message = event.getMessage();
        String content = message.getContentRaw();

        //Parse args
        String args[] = content.split(" ");
        if (args.length == 0) return;

        //Test ping command
        if (args[0].equals("!ping")) {
            printChannel(event.getChannel(), "Pong! We are up and running!");
        }

        //Manual command interface
        if (args[0].equals("!com")) {
            printChannel(event.getChannel(), "No arguments stated");
        }

        //Casual command interface
        String commandCon = content.toLowerCase();
        if (CasualPhraseProcessor.botAddressed(commandCon)) {
            if (CasualPhraseProcessor.botJoinCallCommand(commandCon)) {
                getVCH(event.getGuild()).joinCommandHandler(event);
            } else if (CasualPhraseProcessor.botLeaveCallCommand(commandCon)){
                printChannel(event.getChannel(), "Leaving voice channel...");
                getVCH(event.getGuild()).disconnect(event.getGuild());

            } else {
                int choice = r.nextInt(3);
                if (choice == 0) printChannel(event.getChannel(), "What?");
                if (choice == 1) printChannel(event.getChannel(), "What did you say?");
                if (choice == 2) printChannel(event.getChannel(), "What do you want?");
            }
        }
    }

    private VoiceConnectionHandler getVCH(Guild g) {
        for (VoiceConnectionHandler vch : vchList) {
            if (vch.getName().equals(g.getId())) return vch;
        }

        VoiceConnectionHandler vch = new VoiceConnectionHandler(g, apm);
        vchList.add(vch);

        System.out.println("Registered VCH for " + vch.getName());

        return vch;
    }

    //Prints out to a channel
    private void printChannel(MessageChannel channel, String message) {
        channel.sendMessage(message).queue();
    }

}
