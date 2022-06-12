package com.adamlewandowski.Discord_Bot.service;

import com.adamlewandowski.Discord_Bot.model.CasperUser;
import com.adamlewandowski.Discord_Bot.model.dto.PointsDto;
import com.adamlewandowski.Discord_Bot.persistance.UserRepository;
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
    private static final String BOTTOMREP = "/bottomrep";
    private static final String TOPREP = "/toprep";
    private final UserRepository userRepository;
    private final RankPngGenerator rankPngGenerator;

    public void checkTopLists(MessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();
        if (message.contains(BOTTOMREP) || message.contains(TOPREP)) {
            showTopUsers(event);
        }
    }

    private void showTopUsers(MessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();
        MessageChannel channel = event.getChannel();
        String[] splitString = message.split(" ");
        Integer numberOfUsers = 10;
        if(splitString.length != 1){
            numberOfUsers = Integer.parseInt(splitString[1]);
        }
        List<CasperUser> usersList;
        if (message.contains(TOPREP)) {
            usersList = userRepository.findUsersWithBestReputation(numberOfUsers);
        } else {
            usersList = userRepository.findUsersWithWorstReputation(numberOfUsers);
        }
        List<PointsDto> pointsDtos = usersList.stream()
                .map(p -> new PointsDto(p.getEmailAddress(), p.getAllPoints()))
                .toList();
        channel.sendMessage(pointsDtos.toString()).queue();
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
            Optional<CasperUser> byUserName = userRepository.findByDiscordId(userDiscordId);
            if (byUserName.isPresent()) {
                currentPoints = byUserName.get().getAllPoints();
                userRank = userRepository.getUserRank(currentPoints);
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
            Optional<CasperUser> byUserName = userRepository.findByDiscordId(userDiscordId);
            if (byUserName.isPresent()) {
                currentPoints = byUserName.get().getAllPoints();
            }
            channel.sendMessage(String.format("You currently have %s points!", currentPoints)).queue();
        }
    }
//TODO
    //    private void addPointFromUserToUser(MessageReceivedEvent event) {
//        String message = event.getMessage().getContentRaw();
//        MessageChannel channel = event.getChannel();
//        if (message.equals("+")) {
//            channel.sendMessage("" + event.getResponseNumber())
//                    .queue();
//        }
//    }
}
