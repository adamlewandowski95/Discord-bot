package com.adamlewandowski.Discord_Bot.listeners;

import com.adamlewandowski.Discord_Bot.service.JokeService;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.io.IOException;

public class MessageListeners extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        Message msg = event.getMessage();

        if (msg.getContentRaw().contains("dzięki")) { // można spróbować zrobić na caseach (Seba) - można zrobić na strategii. Klasa posiadająca słownik dobrych słów, druga złych słów.
            MessageChannel channel = event.getChannel();
            channel.sendMessage("przyznano punkt dobroci!").queue();
        }

        if (msg.getContentRaw().equals("!ping")) {
            MessageChannel channel = event.getChannel();
            long time = System.currentTimeMillis();
            channel.sendMessage("Pong!")
                    .queue(response -> response.editMessageFormat("Pong: %d ms", System.currentTimeMillis() - time)
                            .queue());
        } else if (msg.getContentRaw().equals("!dowcip")) {
            MessageChannel channel = event.getChannel();
            JokeService jokeService = new JokeService();
            try {
                channel.sendMessage(jokeService.getJoke()).queue();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
