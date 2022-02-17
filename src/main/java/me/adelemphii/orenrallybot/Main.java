package me.adelemphii.orenrallybot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import me.adelemphii.orenrallybot.discord.DiscordBot;
import me.adelemphii.orenrallybot.utility.Configuration;
import me.adelemphii.orenrallybot.utility.ErrorOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static Logger logger = LoggerFactory.getLogger("OrenRallyBot");
    public static List<StackTraceElement[]> errorLogs = new ArrayList<>();
    public static DiscordBot discordBot;

    public static Configuration config;

    public static void main(String[] args) {
        logger.info("Starting OrenRallyBot..");
        logger.info("Loading configuration..");
        if(loadConfiguration()) {
            logger.info("Configuration loaded!");
        } else {
            logger.error("Failed to load configuration! Exiting..");
            ErrorOutput.log();
            System.exit(1);
        }

        logger.info("Starting bot..");
        discordBot = new DiscordBot();
        logger.info("Use Ctrl+C to exit.");

        // shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Shutting down..");
            discordBot.shutdown();
            ErrorOutput.log();
        }));
    }

    private static boolean loadConfiguration() {
        try {
            // The commented out area is for when testing in intellij
            //ClassLoader loader = Thread.currentThread().getContextClassLoader();
            //InputStream is = loader.getResourceAsStream("exampleConfig.yml");
            InputStream is = new FileInputStream("config.yml");

            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            config = mapper.readValue(is, Configuration.class);
            is.close();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            errorLogs.add(ex.getStackTrace());
            return false;
        }
    }
}
