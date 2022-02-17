package me.adelemphii.orenrallybot.utility;

import me.adelemphii.orenrallybot.Main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ErrorOutput {

    // create a file to log errors to, if it doesn't exist
    private static File file = new File("latest_errors.log");


    // log objects in Main.errorLogs() to the file
    public static void log() {
        try {
            // if the file doesn't exist, create it
            if (!file.exists()) {
                file.createNewFile();
            }

            // create a buffered writer to write to the file
            BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));

            List<StackTraceElement[]> stackTraceElements = Main.errorLogs;

            writer.write("Truthfully, I haven't tested this yet, but I'm pretty sure it works.\n");
            writer.write("I'm not even sure if it works.\n");
            writer.write("It probably doesn't.\n");

            // write each stack trace element to the file
            for (StackTraceElement[] stackTraceElement : stackTraceElements) {
                for (StackTraceElement element : stackTraceElement) {
                    writer.write(element.toString());
                    writer.newLine();
                }
                writer.newLine();
            }

            // close the writer
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
