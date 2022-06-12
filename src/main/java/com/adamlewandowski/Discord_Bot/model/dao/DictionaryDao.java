package com.adamlewandowski.Discord_Bot.model.dao;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name = "DICTIONARY")
@Getter
@Setter
public class DictionaryDao {

    @Id
    @GeneratedValue()
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "word", nullable = false)
    private String word;

    @Column(name = "power", nullable = false)
    private Integer power;
}
