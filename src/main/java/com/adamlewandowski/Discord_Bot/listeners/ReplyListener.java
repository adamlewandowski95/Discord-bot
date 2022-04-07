package com.adamlewandowski.Discord_Bot.listeners;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class ReplyListener extends ListenerAdapter {

    @Override
    public void onMessageUpdate(@NotNull MessageUpdateEvent event) {
        event.getAuthor();

        MessageChannel channel = event.getChannel();
        channel.sendMessage("detected").queue();
    }
}
