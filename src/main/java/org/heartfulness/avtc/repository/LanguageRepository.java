package org.heartfulness.avtc.repository;

import org.heartfulness.avtc.model.Language;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;

import java.util.HashSet;
import java.util.List;

public interface LanguageRepository extends CrudRepository<Language,Long> {
   // void save(Language language);
   List<Language> findByAgentId(Long agentId);

}
