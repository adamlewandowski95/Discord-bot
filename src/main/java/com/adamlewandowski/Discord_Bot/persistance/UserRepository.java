package com.adamlewandowski.Discord_Bot.persistance;

import com.adamlewandowski.Discord_Bot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByDiscordId(Long discordId);

    Optional<User> findByEmail(String email);

    @Query(value = "select * from discord order by all_points desc limit :limit", nativeQuery = true)
    List<User> findUsersWithBestReputation(@Param("limit")Integer limit);

    @Query(value = "select * from discord order by all_points asc limit :limit", nativeQuery = true)
    List<User> findUsersWithWorstReputation(@Param("limit")Integer limit);

    @Query(value = "select count(id) + 1 from discord where all_points > :userPoints", nativeQuery = true)
    Long getUserRank(@Param("userPoints")Integer userPoints);
}