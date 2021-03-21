package com.tergav17.bot;

import javafx.scene.control.RadioMenuItem;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.entities.TextChannel;

import java.io.File;
import java.util.List;
import java.util.Random;

public class SpeechCommandDispatch extends Thread{
    private byte[] clip;
    private VoiceChannelHandler channelHandler;
    private VoiceConnectionHandler connectionHandler;
    private long dispatchTime;

    public SpeechCommandDispatch(byte[] clip, VoiceChannelHandler channelHandler, VoiceConnectionHandler connectionHandler, long dispatchTime) {
        this.clip = clip;
        this.channelHandler = channelHandler;
        this.connectionHandler = connectionHandler;
        this.dispatchTime = dispatchTime;
    }

    public void run() {
        String command = connectionHandler.sph.recognizeString(AudioConversionUtils.toSphinxAudio(clip));

        System.out.println("Command: '" + command + "'");

        String args[] = command.split(" ");


        if (args.length < 2) return;

        if (args[0].equals("yard") || args[1].equals("bot")) {
            String commandToProcess = "null";

            for (int i = 2; i < args.length; i++) {
                String arg = args[i];

                if (commandToProcess.equals("null") && arg.equals("hello")) {
                    connectionHandler.playAudio("etc/soundbytes/hello.wav");

                    return;
                }

                else if (commandToProcess.equals("null") && arg.equals("leave") && i == args.length-1) {
                    connectionHandler.playAudio("etc/soundbytes/bye.wav");

                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    connectionHandler.disconnect(connectionHandler.guild);


                    return;
                }

                else if (commandToProcess.equals("null") && arg.equals("funny")) {
                    File[] dir = new File("etc/soundbytes/funnys").listFiles();

                    Random r = new Random();

                    connectionHandler.playAudio(dir[r.nextInt(dir.length)].getAbsolutePath());

                    return;
                }

                else if (commandToProcess.equals("null") && arg.equals("stop")) {
                    connectionHandler.playAudio("etc/soundbytes/stop.wav");

                    if (connectionHandler.arb.isRecording()) {
                        int blocks = (int) ((dispatchTime - connectionHandler.arb.getRecordingStart()) / 20);
                        int offset = (int) ((System.currentTimeMillis() - dispatchTime) / 20);

                        AudioConversionUtils.writeDiscordAudioToFile(connectionHandler.arb.clip(blocks, offset));

                        connectionHandler.arb.stopRecording();
                        SavedClipDeployer.deployClip(connectionHandler.guild);
                    }

                    return;
                }

                else if (commandToProcess.equals("null") && arg.equals("clip")) {
                    connectionHandler.playAudio("etc/soundbytes/clip.wav");

                    int offset = (int) ((System.currentTimeMillis() - dispatchTime) / 20);

                    AudioConversionUtils.writeDiscordAudioToFile(connectionHandler.arb.clip(1500, offset));

                    SavedClipDeployer.deployClip(connectionHandler.guild);

                    return;
                }

                else if (commandToProcess.equals("null") && arg.equals("record")) {
                    connectionHandler.playAudio("etc/soundbytes/recording.wav");

                    connectionHandler.arb.startRecording();

                    return;
                }

                else if (commandToProcess.equals("null") && arg.equals("play")) {
                    connectionHandler.playAudio("etc/soundbytes/play.wav");

                    try {
                        Thread.sleep(2500);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    List<TextChannel> channels = connectionHandler.guild.getTextChannels();

                    for (TextChannel chan : channels) {
                        if(chan.getName().startsWith("no-mic")) {
                            MessageHistory history = chan.getHistory();

                            history.retrievePast(100).complete();

                            List<Message> messages = history.getRetrievedHistory();

                            ScanningItemLoader sil = new ScanningItemLoader(connectionHandler, messages);

                            sil.loadElement(0);
                        }
                    }

                    return;
                }
            }
        }
    }
}
