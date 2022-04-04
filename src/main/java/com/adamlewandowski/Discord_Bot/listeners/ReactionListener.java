package com.adamlewandowski.Discord_Bot.listeners;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.MessageReaction.ReactionEmote;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;

@Component
public class ReactionListener extends ListenerAdapter {

    @Override
    public void onMessageReactionAdd(@Nonnull MessageReactionAddEvent event) {
        //trzeab sprawdzic właściciela posta i właściciela emotki, czy nie jest taki sam
        Member member = event.getMember();
//        if (msg.getAuthor().equals()
        MessageReaction reaction = event.getReaction();
        ReactionEmote reactionEmote = reaction.getReactionEmote(); // tutaj po nazwie lub id emotki możesz pojechać, możesz potestować na poście i zrobic sobie wyświetlanie id do system.out
//        System.out.println(reactionEmote.getIdLong()); np tak
        MessageChannel channel = event.getChannel();
        channel.sendMessage(member + "dodał emotke!").queue();
    }

    @Override
    public void onMessageReactionRemove(@Nonnull MessageReactionRemoveEvent event) {
        MessageChannel channel = event.getChannel();
        channel.sendMessage("Ktoś usunal emotke!").queue();
    }
}
