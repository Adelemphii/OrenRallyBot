package me.adelemphii.orenrallybot.discord.listeners;

import me.adelemphii.orenrallybot.Main;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class LogMessagesToConsole extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if(event.getAuthor().isBot()) {
            return;
        }

        Main.logger.info("[" + event.getGuild().getName() + "] " + event.getAuthor().getName() + ": " + event.getMessage().getContentRaw());
    }
}
