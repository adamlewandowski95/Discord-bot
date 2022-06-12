package com.adamlewandowski.Discord_Bot.persistance;

import com.adamlewandowski.Discord_Bot.model.CasperUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<CasperUser, Long> {

    Optional<CasperUser> findByDiscordId(Long discordId);

    @Query(value = "select * from discord order by all_points desc limit :limit", nativeQuery = true)
    List<CasperUser> findUsersWithBestReputation(@Param("limit")Integer limit);

    @Query(value = "select * from discord order by all_points asc limit :limit", nativeQuery = true)
    List<CasperUser> findUsersWithWorstReputation(@Param("limit")Integer limit);

    @Query(value = "select count(id) + 1 from discord where all_points > :userPoints", nativeQuery = true)
    Long getUserRank(@Param("userPoints")Integer userPoints);
}