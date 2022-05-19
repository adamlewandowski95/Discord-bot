package com.adamlewandowski.Discord_Bot.persistance;

import com.adamlewandowski.Discord_Bot.model.DailyPoints;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;

public interface DailyLogRepository extends JpaRepository<DailyPoints, Long> {

        @Query(value = "select max(date) from disclog where user_id = :userDiscordId", nativeQuery = true)
        LocalDateTime getDateOfLastUserPost (Long userDiscordId);

        @Query(value = "select * from disclog where user_id = :userDiscordId limit 1", nativeQuery = true)
        Optional<DailyPoints> findOneByUserId(Long userDiscordId);
}