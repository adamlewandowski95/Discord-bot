package com.adamlewandowski.Discord_Bot.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "USERS", indexes = {@Index(columnList = "emailAddress")})
@ToString
public class CasperUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String emailAddress;
    private String casperUsername;
    private Long discordId;
    private String telegramName; //nie wiem po czym szukamy w telegramie więc dam na razie string username

    private int csvPoints;
    private int discordPoints;
    private int telegramPoints;
    private int allPoints;
}