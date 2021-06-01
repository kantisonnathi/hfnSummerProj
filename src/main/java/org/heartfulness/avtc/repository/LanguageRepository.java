package org.heartfulness.avtc.repository;

import org.heartfulness.avtc.model.Language;
import org.springframework.data.repository.Repository;

import java.util.HashSet;
import java.util.List;

public interface LanguageRepository extends Repository<Language,Long> {
    void save(Language language);
   HashSet<Language> findByAgentId(Integer agentId);

}
