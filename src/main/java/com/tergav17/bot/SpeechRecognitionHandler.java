package com.tergav17.bot;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.api.StreamSpeechRecognizer;

import javax.sound.sampled.AudioInputStream;

public class SpeechRecognitionHandler {

    private StreamSpeechRecognizer recognizer;

    private boolean isRunning = false;

    public SpeechRecognitionHandler() {
        Configuration configuration = new Configuration();

        configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
        configuration.setDictionaryPath("resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict");
        //configuration.setLanguageModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us.lm.bin");

        configuration.setGrammarPath("file:etc/grammars");
        configuration.setGrammarName("grammar");
        configuration.setUseGrammar(true);

        try {
            recognizer = new StreamSpeechRecognizer(configuration);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String recognizeString(AudioInputStream ais) {
        while (isRunning) {
            try {
                System.out.println("Speech recognition standby...");
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        isRunning = true;
        recognizer.startRecognition(ais);
        SpeechResult res;
        String out = "";

        while ((res = recognizer.getResult()) != null) {
            out = res.getHypothesis();
        }

        recognizer.stopRecognition();
        isRunning = false;

        return out;
    }
}
