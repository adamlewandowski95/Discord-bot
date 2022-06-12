package com.adamlewandowski.Discord_Bot.persistance;

import com.adamlewandowski.Discord_Bot.model.dao.DictionaryDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DictionaryRepository extends JpaRepository<DictionaryDao, Long> {

}