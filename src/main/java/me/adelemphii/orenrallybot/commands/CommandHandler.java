package me.adelemphii.orenrallybot.commands;

import me.adelemphii.orenrallybot.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CommandHandler extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if(event.getAuthor().isBot()) return;
        // check if the author has any of the roles in the config
        if(event.getMember() != null && event.getMember().getRoles().stream().noneMatch(role -> Main.config.getAdminRoles().contains(Long.parseLong(role.getId())))) {
            createEmbed(event.getJDA().getSelfUser(), "You do not have permission to use this command.", event, false);
            return;
        }

        String[] args = event.getMessage().getContentRaw().split(" ");
        String command = args[0];
        if (command.startsWith(Main.config.getPrefix())) {
            command = command.substring(1);

            if(command.equalsIgnoreCase("wc")) {
                // !wc <channelID> <messageID> then send an embed with the text of the messageID
                // this code is UGLY AF
                if (args.length == 3) {
                    String channelID = args[1];
                    String messageID = args[2];

                    try {
                        Long.parseLong(channelID);
                    } catch (NumberFormatException e) {
                        createEmbed(event.getJDA().getSelfUser(), "Invalid channel ID", event, false);
                        return;
                    }

                    TextChannel channel = event.getJDA().getTextChannelById(channelID);
                    if(channel == null) {
                        createEmbed(event.getJDA().getSelfUser(), "Invalid channel ID", event, false);
                    } else {
                        try {
                            Long.parseLong(messageID);
                        } catch(NumberFormatException e) {
                            createEmbed(event.getJDA().getSelfUser(), "Invalid message ID", event, false);
                            return;
                        }

                        Message message = channel.retrieveMessageById(messageID).complete();
                        if(message == null) {
                            createEmbed(event.getJDA().getSelfUser(), "Invalid message ID", event, false);
                        } else {
                            createEmbed(event.getAuthor(), message.getContentRaw(), event, true);
                        }
                    }
                } else {
                    createEmbed(event.getJDA().getSelfUser(), "Invalid arguments\nExpected: " + Main.config.getPrefix() + "wc <channelID> <messageID>", event, false);
                }
            }
        }
    }

    private void createEmbed(User author, String message, MessageReceivedEvent event, boolean dms) {
        // Create an embed with the author, message, and timestamp
        EmbedBuilder embed = new EmbedBuilder();
        embed.setAuthor(author.getName(), null, author.getAvatarUrl());
        embed.setDescription(message);

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss");

        String formattedDate = now.format(myFormatObj);
        embed.setFooter(formattedDate, null);

        embed.setColor(Color.decode(Main.config.getEmbedColor()));

        if(dms) {
            // get every member with a specific role and dm the embed to them
            List<List<Member>> members = new ArrayList<>();

            for (Long roleID : Main.config.getAdminRoles()) {
                String roleIDString = String.valueOf(roleID);

                List<Member> memberList = event.getGuild().getMembersWithRoles(event.getGuild().getRoleById(roleIDString));
                members.add(memberList);
            }

            for (List<Member> members1 : members) {
                for (Member member : members1) {
                    member.getUser().openPrivateChannel().queue(msg -> {
                        msg.sendMessageEmbeds(embed.build()).queue();
                    });
                }
            }
        } else {
            event.getChannel().sendMessageEmbeds(embed.build()).queue();
        }
    }
}
