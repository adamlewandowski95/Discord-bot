package com.adamlewandowski.Discord_Bot.persistance;

import com.adamlewandowski.Discord_Bot.model.DiscordPoints;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;

public interface DailyLogRepository extends JpaRepository<DiscordPoints, Long> {

        @Query(value = "select max(date) from discord where user_id = :userDiscordId and points > 0", nativeQuery = true)
        Optional<LocalDateTime> getDateOfLastPositiveUserPoints(Long userDiscordId);

        @Query(value = "select * from discord where user_id = :userDiscordId limit 1", nativeQuery = true)
        Optional<DiscordPoints> findOneByUserId(Long userDiscordId);
}