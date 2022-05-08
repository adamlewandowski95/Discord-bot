package com.adamlewandowski.Discord_Bot.service;

import com.adamlewandowski.Discord_Bot.model.DiscordUserPoints;
import com.adamlewandowski.Discord_Bot.model.dto.DiscordPointsDto;
import com.adamlewandowski.Discord_Bot.persistance.DiscordUserRepository;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserCommandsService {
    private static final String bottom = "/bottomrep";
    private static final String top = "/toprep";
    private final DiscordUserRepository discordUserRepository;
    private final RankPngGenerator rankPngGenerator;

    public void checkTopLists(MessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();
        if (message.contains(bottom) || message.contains(top)) {
            showTopUsers(event);
        }
    }

    private void showTopUsers(MessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();
        MessageChannel channel = event.getChannel();
        String[] splitString = message.split(" ");
        Integer numberOfUsers = 10;
        //todo zablokować opcje limit 0
        if(splitString.length != 1){
            numberOfUsers = Integer.parseInt(splitString[1]);
        }
        List<DiscordUserPoints> usersList;
        if (message.contains(top)) {
            usersList = discordUserRepository.findUsersWithBestReputation(numberOfUsers);
        } else {
            usersList = discordUserRepository.findUsersWithWorstReputation(numberOfUsers);
        }
        List<DiscordPointsDto> discordPointsDtos = usersList.stream()
                .map(p -> new DiscordPointsDto(p.getUserName(), p.getAllPoints()))
                .toList();
        channel.sendMessage(discordPointsDtos.toString()).queue();
    }

    public void checkMyRep(MessageReceivedEvent event) {
        Member member = event.getMember();
        String effectiveName = member.getEffectiveName();
        String avatarUrl = member.getEffectiveAvatarUrl();
        Long userDiscordId = member.getIdLong();
        String message = event.getMessage().getContentRaw();
        MessageChannel channel = event.getChannel();
        Integer currentPoints;
        Long userRank;
        File file = null;
        if (message.contains("/rank")) {
            Optional<DiscordUserPoints> byUserName = discordUserRepository.findByUserId(userDiscordId);
            if (byUserName.isPresent()) {
                currentPoints = byUserName.get().getAllPoints();
                userRank = discordUserRepository.getUserRank(currentPoints);
                try {
                    file = rankPngGenerator.loadImageAndAddText(effectiveName, avatarUrl, userRank, currentPoints);
                } catch (IOException e) {
                    e.getMessage();
                }
                channel.sendMessage(" ").addFile(file).queue();
            }
        }
    }

    public void checkMyPoints(MessageReceivedEvent event) {
        Member member = event.getMember();
        Long userDiscordId = member.getIdLong();
        String message = event.getMessage().getContentRaw();
        MessageChannel channel = event.getChannel();
        Integer currentPoints = 0;
        if (message.contains("/me")) {
            Optional<DiscordUserPoints> byUserName = discordUserRepository.findByUserId(userDiscordId);
            if (byUserName.isPresent()) {
                currentPoints = byUserName.get().getAllPoints();
            }
            channel.sendMessage(String.format("You currently have %s points!", currentPoints)).queue();
        }
    }

    //    private void addPointFromUserToUser(MessageReceivedEvent event) {
//        String message = event.getMessage().getContentRaw();
//        MessageChannel channel = event.getChannel();
//        if (message.equals("+")) {
//            channel.sendMessage("" + event.getResponseNumber())
//                    .queue();
//        }
//    }
}
