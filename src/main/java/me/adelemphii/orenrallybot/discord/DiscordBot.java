package me.adelemphii.orenrallybot.discord;

import me.adelemphii.orenrallybot.Main;
import me.adelemphii.orenrallybot.commands.WarclaimMessageCommand;
import me.adelemphii.orenrallybot.discord.listeners.LogMessagesToConsole;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;
import java.util.*;

public class DiscordBot {

    private JDA jda;
    private static DiscordBot instance = null;

    public DiscordBot() {
        instance = this;

        try {
            this.jda = buildJDA();
        } catch (LoginException e) {
            Main.errorLogs.add(e.getStackTrace());
            e.printStackTrace();
        }

        registerEvents();
    }

    public void shutdown() {
        jda.shutdown();
    }

    public JDA getJDA() {
        return this.jda;
    }

    public static DiscordBot getInstance() {
        return Objects.requireNonNullElseGet(instance, DiscordBot::new);
    }

    private JDA buildJDA() throws LoginException {
        JDABuilder builder = JDABuilder.createDefault(Main.config.getToken());

        builder.setActivity(Activity.competing(Main.config.getStatus()));
        builder.setStatus(OnlineStatus.ONLINE);
        return builder.build();
    }

    private void registerEvents() {
        jda.addEventListener(new LogMessagesToConsole());
        jda.addEventListener(new WarclaimMessageCommand());
    }
}
