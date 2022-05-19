package com.adamlewandowski.Discord_Bot.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "USERS")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CasperUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private Long discordId;

    private Long telegramId;

    @Setter
    private Integer allPoints;
}