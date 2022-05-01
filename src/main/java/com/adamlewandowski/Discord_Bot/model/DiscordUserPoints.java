package com.adamlewandowski.Discord_Bot.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "DISCORD")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DiscordUserPoints {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userName;

    private Long userId;

    @Setter
    private Integer allPoints;

    public void addPoints(Integer newPoints) {
        allPoints += newPoints;
    }

    public void subtractPoints(Integer newPoints) {
        allPoints -= newPoints;
    }
}