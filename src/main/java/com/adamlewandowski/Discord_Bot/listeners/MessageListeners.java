package com.adamlewandowski.Discord_Bot.listeners;

import com.adamlewandowski.Discord_Bot.service.AdminCommandsService;
import com.adamlewandowski.Discord_Bot.service.ChannelService;
import com.adamlewandowski.Discord_Bot.service.UserCommandsService;
import com.adamlewandowski.Discord_Bot.service.WordsService;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MessageListeners extends ListenerAdapter {

    private final ChannelService channelService;
    private final UserCommandsService userCommandsService;
    private final AdminCommandsService adminCommandsService;
    private final WordsService wordsService;


    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        if (channelService.isBotRespond(event)) {
            return;
        }

        if (!channelService.isChannelListened(event)) {
            return;
        }

        wordsService.checkMessageForGoodAndBadWords(event);
        adminCommandsService.checkAdminCommands(event);
        userCommandsService.checkMyPoints(event);
        userCommandsService.checkMyRep(event);
        userCommandsService.checkTopLists(event);
        checkPing(event);
        //        addPointFromUserToUser(event);
    }

    private void checkPing(MessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw().toLowerCase();
        if (message.equals("!ping")) {
            MessageChannel channel = event.getChannel();
            long time = System.currentTimeMillis();
            channel.sendMessage("Pong!")
                    .queue(response -> response.editMessageFormat("Pong: %d ms", System.currentTimeMillis() - time)
                            .queue());
        }
    }
}