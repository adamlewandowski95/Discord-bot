package com.adamlewandowski.Discord_Bot.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "DISCORD")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DiscordUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userName;

    private Long userId;

    @Setter
    private Integer points;
}