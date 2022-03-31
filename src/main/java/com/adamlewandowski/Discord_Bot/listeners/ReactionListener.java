package com.adamlewandowski.Discord_Bot.listeners;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.MessageReaction.ReactionEmote;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class ReactionListener extends ListenerAdapter {

    @Override
    public void onMessageReactionAdd(@Nonnull MessageReactionAddEvent event) {
        //trzeab sprawdzic właściciela posta i właściciela emotki, czy nie jest taki sam
        Member member = event.getMember();
//        if (msg.getAuthor().equals()
        MessageReaction reaction = event.getReaction();
        ReactionEmote reactionEmote = reaction.getReactionEmote();
        MessageChannel channel = event.getChannel();
        channel.sendMessage("Ktoś dodał emotke!").queue();
    }

    @Override
    public void onMessageReactionRemove(@Nonnull MessageReactionRemoveEvent event) {
        MessageChannel channel = event.getChannel();
        channel.sendMessage("Ktoś usunal emotke!").queue();
    }
}
