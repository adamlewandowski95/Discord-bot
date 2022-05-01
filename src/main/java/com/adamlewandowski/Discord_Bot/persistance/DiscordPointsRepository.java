package com.adamlewandowski.Discord_Bot.persistance;

import com.adamlewandowski.Discord_Bot.model.DiscordUserPoints;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DiscordPointsRepository extends JpaRepository<DiscordUserPoints, Long> {

    Optional<DiscordUserPoints> findByUserId(Long userId);

    Optional<DiscordUserPoints> findByUserName(String userName);

    @Query(value = "select * from discord order by points desc limit :limit", nativeQuery = true)
    List<DiscordUserPoints> findUsersWithBestReputation(@Param("limit")Integer limit);

    @Query(value = "select * from discord order by points asc limit :limit", nativeQuery = true)
    List<DiscordUserPoints> findUsersWithWorstReputation(@Param("limit")Integer limit);

    @Query(value = "select count(id) + 1 from discord where points > :userPoints", nativeQuery = true)
    Long getUserRank(@Param("userPoints")Integer userPoints);
}