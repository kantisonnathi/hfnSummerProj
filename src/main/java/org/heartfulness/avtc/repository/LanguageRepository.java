package org.heartfulness.avtc.repository;

import org.heartfulness.avtc.model.Language;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LanguageRepository extends CrudRepository<Language,Long> {

   //Language save(Language language);
   List<Language> findByAgentId(Long agentId);

   List<Language> findAll();

}
