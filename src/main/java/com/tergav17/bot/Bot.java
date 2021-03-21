package com.tergav17.bot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

public class Bot {
    public static void main(String[] arguments) throws Exception {
        //JDA api = JDABuilder.createDefault("ODIxMTUxODA5Mjc5MDk4OTIy.YE_jNw.2ouXf_swBkU515oxxo4o_Q7swC4").build();

        //Get the key from a key file
        System.out.println("Fetching key file...");
        File f = new File("bot.key");

        //If it does not exist, allow it to be created from the command line
        if (!f.exists()) {
            f.createNewFile();

            System.out.print("Key file does not exist!\nEnter key:");
            Scanner termIn = new Scanner(System.in);

            FileWriter writer = new FileWriter(f);

            writer.write(termIn.nextLine());

            writer.close();
            termIn.close();
        }

        //Grab the key from the file
        System.out.println("Attempting to load key...");

        //Read it
        Scanner fileIn = new Scanner(f);
        String key = fileIn.nextLine();
        fileIn.close();

        System.out.println("Found key: " + key);

        //API setup
        JDA api = JDABuilder.createDefault(key).build();

        //Register listener
        api.addEventListener(new BotEventListener());

        //Command line loop
        Scanner sc = new Scanner(System.in);
        while (true) {
            String nextLine = sc.nextLine();

            if (nextLine.equals("exit")) {
                System.exit(0);
            }
        }
    }


}
