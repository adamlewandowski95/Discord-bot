package com.adamlewandowski.Discord_Bot.listeners;

import com.adamlewandowski.Discord_Bot.model.User;
import com.adamlewandowski.Discord_Bot.persistance.UserRepository;
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
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ReactionListener extends ListenerAdapter {
    private final UserRepository userRepository;

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
        net.dv8tion.jda.api.entities.User author = messageRestAction.complete().getAuthor();
        MessageReaction reaction = event.getReaction();
        ReactionEmote reactionEmote = reaction.getReactionEmote();
        boolean isLikeEmote = reactionEmote.toString().equals("RE:U+1f44d");
        boolean isAuthorAMember = author.getId().equals(memberId);
        if (isLikeEmote && !isAuthorAMember) {
            modifyAuthorPoints(action, author);
        }
    }

    private void modifyAuthorPoints(String action, net.dv8tion.jda.api.entities.User author) {
        Optional<User> authorFromDb = userRepository.findByDiscordId(Long.parseLong(author.getId()));
        User messageAuthor = authorFromDb.orElseGet(() -> User.builder()
                .discordId(Long.parseLong(author.getId()))
                .email(author.getName())
                .allPoints(0)
                .build());
        if (action.equals("add")) {
            messageAuthor.addPoints(1);
        } else {
            messageAuthor.subtractPoints(1);
        }
        userRepository.save(messageAuthor);
    }
}