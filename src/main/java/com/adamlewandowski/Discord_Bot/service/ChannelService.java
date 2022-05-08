package com.adamlewandowski.Discord_Bot.service;

import com.adamlewandowski.Discord_Bot.configuration.DiscordBotConfiguration;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ChannelService {

    private final DiscordBotConfiguration configuration;

    public boolean isChannelListened(MessageReceivedEvent event){
        boolean result = false;
        String parentCategoryId = event.getTextChannel().getParentCategoryId();
        if (parentCategoryId != null) {
            long l = Long.parseLong(parentCategoryId);
            Category currentCategory = event.getGuild().getCategoryById(l);
            String currentCategoryName = currentCategory.getName();
            MessageChannel currentChannel = event.getChannel();
            Map<String, List<String>> listenedCategories = configuration.getListenedCategories();
            if (listenedCategories.containsKey(currentCategoryName)){
                List<String> channelsInCurrentCategory = listenedCategories.get(currentCategoryName);
                if(channelsInCurrentCategory.isEmpty() || channelsInCurrentCategory.contains(currentChannel.getName())){
                    result = true;
                }
            }
        }
        return result;
    }

    public boolean isBotRespond(MessageReceivedEvent event){
        return event.getMember().getEffectiveName().equals(configuration.getBotName());
    }
}
