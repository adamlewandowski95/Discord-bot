package com.adamlewandowski.Discord_Bot.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "DISCLOG")
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DailyPoints {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userName;

    private Long userId;

    private Integer points;

    private LocalDateTime date;
}
