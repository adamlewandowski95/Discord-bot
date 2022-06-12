package com.adamlewandowski.Discord_Bot.listeners;

import com.adamlewandowski.Discord_Bot.model.DiscordPoints;
import com.adamlewandowski.Discord_Bot.persistance.DailyLogRepository;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.MessageReaction.ReactionEmote;
import net.dv8tion.jda.api.events.message.react.GenericMessageReactionEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.RestAction;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ReactionListener extends ListenerAdapter {
    private final DailyLogRepository dailyLogRepository;

    @Override
    public void onMessageReactionAdd(@Nonnull MessageReactionAddEvent event) {
        onLikeAction(event, "add");
    }

    @Override
    public void onMessageReactionRemove(@Nonnull MessageReactionRemoveEvent event) {
        onLikeAction(event, "remove");
    }

    private void onLikeAction(GenericMessageReactionEvent event, String action) {
        Member member = event.getMember();
        String memberId = member.getId();
        MessageChannel channel = event.getChannel();
        RestAction<Message> messageRestAction = channel.retrieveMessageById(event.getMessageId());
        User author = messageRestAction.complete().getAuthor();
        MessageReaction reaction = event.getReaction();
        ReactionEmote reactionEmote = reaction.getReactionEmote();
        boolean isLikeEmote = reactionEmote.toString().equals("RE:U+1f44d");
        boolean isAuthorAMember = author.getId().equals(memberId);
        if (isLikeEmote && !isAuthorAMember) {
            modifyAuthorPoints(action, author);
        }
    }

    //todo sprecyzować kto ma dostac pkt (osoba dajaća lajka czy osoba obdarowana likem)
    private void modifyAuthorPoints(String action, User author) {
        int pointsFromLike;
//        Optional<CasperUser> authorFromDb = userRepository.findByDiscordId(Long.parseLong(author.getId()));
//        CasperUser messageAuthor = authorFromDb.orElseGet(() -> CasperUser.builder()
//                .discordId(Long.parseLong(author.getId()))
//                .email(author.getName())
//                .allPoints(0)
//                .build());

        if (action.equals("add")) {
            pointsFromLike = 1;
        } else {
            pointsFromLike = -1;
        }

        DiscordPoints discordPoints = DiscordPoints.builder()
                .userId(author.getIdLong())
                .userName(author.getName())
                .points(pointsFromLike)
                .date(LocalDateTime.now())
                .build();
        dailyLogRepository.save(discordPoints);
    }
}