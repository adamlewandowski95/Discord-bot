package com.adamlewandowski.Discord_Bot.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "DISCORD")
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DiscordPoints {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userName;

    private Long userId;

    private Integer points;

    private LocalDateTime date;
}
