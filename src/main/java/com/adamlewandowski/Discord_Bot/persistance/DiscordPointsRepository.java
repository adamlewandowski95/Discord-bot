package com.adamlewandowski.Discord_Bot.persistance;

import com.adamlewandowski.Discord_Bot.model.DiscordUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DiscordPointsRepository extends JpaRepository<DiscordUser, Long> {

    Optional<DiscordUser> findByUserId(Long userId);

    Optional<DiscordUser> findByUserName(String userName);

    @Query(value = "select * from discord order by points desc limit :limit", nativeQuery = true)
    List<DiscordUser> findUsersWithBestReputation(@Param("limit")Integer limit);

    @Query(value = "select * from discord order by points asc limit :limit", nativeQuery = true)
    List<DiscordUser> findUsersWithWorstReputation(@Param("limit")Integer limit);


}