package com.adamlewandowski.Discord_Bot.persistance;

import com.adamlewandowski.Discord_Bot.model.DiscordUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DiscordPointsRepository extends JpaRepository<DiscordUser, Long> {

    Optional<DiscordUser> findByUserId(Long userId);

}