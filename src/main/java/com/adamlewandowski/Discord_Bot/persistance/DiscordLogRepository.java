package com.adamlewandowski.Discord_Bot.persistance;

import com.adamlewandowski.Discord_Bot.model.DiscordLogPoints;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DiscordLogRepository extends JpaRepository<DiscordLogPoints, Long> {

//    @Query("update discord set all_points = (select sum(points) from disclog where user_id = :userDiscordId) where user_id = :userDiscordId")
//    public void updateUserPoints(Long userDiscordId);
}
