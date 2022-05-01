package com.adamlewandowski.Discord_Bot.listeners;

import com.adamlewandowski.Discord_Bot.configuration.DiscordBotConfiguration;
import com.adamlewandowski.Discord_Bot.model.DiscordLogPoints;
import com.adamlewandowski.Discord_Bot.model.DiscordUserPoints;
import com.adamlewandowski.Discord_Bot.model.dto.DiscordPointsDto;
import com.adamlewandowski.Discord_Bot.persistance.DiscordLogRepository;
import com.adamlewandowski.Discord_Bot.persistance.DiscordPointsRepository;
import com.adamlewandowski.Discord_Bot.service.PointsCalculator;
import com.adamlewandowski.Discord_Bot.service.RankPngGenerator;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class MessageListeners extends ListenerAdapter {
    private final DiscordPointsRepository discordPointsRepository;
    private final DiscordLogRepository discordLogRepository;
    private final PointsCalculator pointsCalculator;
    private final DiscordBotConfiguration discordBotConfiguration;
    private final RankPngGenerator rankPngGenerator;

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        if (event.getMember().getEffectiveName().equals("Najs Bot")) {
            return;
        }
        checkMessageForGoodAndBadWords(event);
        checkAdminCommands(event);
        checkPing(event);
        checkMyPoints(event);
        checkMyRep(event);
        checkTopLists(event);
//        addPointFromUserToUser(event);
    }

//    private void addPointFromUserToUser(MessageReceivedEvent event) {
//        String message = event.getMessage().getContentRaw();
//        MessageChannel channel = event.getChannel();
//        if (message.equals("+")) {
//            channel.sendMessage("" + event.getResponseNumber())
//                    .queue();
//        }
//    }

    private void checkTopLists(MessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();
        if (message.contains("/bottomrep")) {
            showTopUsers(event, "asc");
        } else if (message.contains("/toprep")) {
            showTopUsers(event, "desc");
        }
    }

    private void showTopUsers(MessageReceivedEvent event, String sort) {
        String message = event.getMessage().getContentRaw();
        MessageChannel channel = event.getChannel();
        String[] splitString = message.split(" ");
        Integer numberOfUsers = Integer.parseInt(splitString[1]);
        List<DiscordUserPoints> usersList;
        if (sort.equals("desc")) {
            usersList = discordPointsRepository.findUsersWithBestReputation(numberOfUsers);
        } else {
            usersList = discordPointsRepository.findUsersWithWorstReputation(numberOfUsers);
        }
        List<DiscordPointsDto> discordPointsDtos = usersList.stream()
                .map(p -> new DiscordPointsDto(p.getUserName(), p.getAllPoints()))
                .toList();
        channel.sendMessage(discordPointsDtos.toString()).queue();
    }

    private void checkMyPoints(MessageReceivedEvent event) {
        Member member = event.getMember();
        Long userDiscordId = member.getIdLong();
        String message = event.getMessage().getContentRaw();
        MessageChannel channel = event.getChannel();
        Integer currentPoints = 0;
        if (message.contains("/me")) {
            Optional<DiscordUserPoints> byUserName = discordPointsRepository.findByUserId(userDiscordId);
            if (byUserName.isPresent()) {
                currentPoints = byUserName.get().getAllPoints();
            }
            channel.sendMessage(String.format("You currently have %s points!", currentPoints)).queue();
        }
    }

    private void checkMyRep(MessageReceivedEvent event) {
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
            Optional<DiscordUserPoints> byUserName = discordPointsRepository.findByUserId(userDiscordId);
            if (byUserName.isPresent()) {
                currentPoints = byUserName.get().getAllPoints();
                userRank = discordPointsRepository.getUserRank(currentPoints);
                try {
                    file = rankPngGenerator.loadImageAndAddText(effectiveName, avatarUrl, userRank, currentPoints);
                } catch (IOException e) {
                    e.getMessage();
                }
                channel.sendMessage(" ").addFile(file).queue();
            }
        }
    }

    // to powinno dodawać wpis do logów
    private void checkAdminCommands(MessageReceivedEvent event) {
        Member member = event.getMember();
        List<Role> roles = member.getRoles();
        String message = event.getMessage().getContentRaw();
        for (String role : discordBotConfiguration.getAuthorizedRoles()) {
            if (roles.toString().contains(role)) {
                if (message.contains("/addpoint")) {
                    changeUserPoints(message, true);
                }
                if (message.contains("/subpoint")) {
                    changeUserPoints(message, false);
                }
                System.out.println("Authorized");
            }
        }
    }

    private void changeUserPoints(String message, boolean isPositivePoints) {
        String[] splitString = message.split(" ");
        String userName = splitString[1];
        int userPointsFromAdmin = Integer.parseInt(splitString[2]);

        Optional<DiscordUserPoints> byUserName = discordPointsRepository.findByUserName(userName);
        if (byUserName.isPresent()) {
            DiscordUserPoints discordUserPoints = byUserName.orElseThrow(() ->
                    new EntityNotFoundException("Bad username or user is not in Db (he should write a message at least once)"));
            if (isPositivePoints) {

//                discordUserPoints.addPoints(userPointsFromAdmin);
            } else {
                userPointsFromAdmin = userPointsFromAdmin * (-1);
//                discordUserPoints.subtractPoints(userPointsFromAdmin);
            }
            DiscordLogPoints discordLogPoints = DiscordLogPoints.builder()
                    .userId(discordUserPoints.getUserId())
                    .userName(discordUserPoints.getUserName())
                    .points(userPointsFromAdmin)
                    .date(new Timestamp(System.currentTimeMillis()))
                    .build();

            discordLogRepository.save(discordLogPoints);

//            discordPointsRepository.save(discordUserPoints);
        }
    }

    private void checkMessageForGoodAndBadWords(MessageReceivedEvent event) {
        Member member = event.getMember();
        Long userDiscordId = member.getIdLong();
        String message = event.getMessage().getContentRaw().toLowerCase();

        // To powinno być w metodzie podliczającej punkty chyba z discord log do discord points
        Optional<DiscordUserPoints> userFromRepo = discordPointsRepository.findByUserId(userDiscordId);
        if(!userFromRepo.isPresent()){
            DiscordUserPoints newDiscordUser = DiscordUserPoints.builder()
                    .userId(userDiscordId)
                    .userName(member.getEffectiveName())
                    .allPoints(0)
                    .build();

            discordPointsRepository.save(newDiscordUser);
//            DiscordUserPoints discordUserPoints = userFromRepo.orElseGet(() -> DiscordUserPoints.builder()
//                    .userId(userDiscordId)
//                    .userName(member.getEffectiveName())
//                    .allPoints(0)
//                    .build());
        }
        // %%%%%%%%%%%%%%%%

        Integer pointsForMessage = pointsCalculator.checkPointsForMessage(message);
//        discordUserPoints.addPoints(pointsForMessage);
//        discordPointsRepository.save(discordUserPoints);
        if (pointsForMessage != 0) {
            DiscordLogPoints discordLogPoints = DiscordLogPoints.builder()
                    .userId(userDiscordId)
                    .userName(member.getEffectiveName())
                    .points(pointsForMessage)
                    .date(new Timestamp(System.currentTimeMillis()))
                    .build();

            discordLogRepository.save(discordLogPoints);
        }
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