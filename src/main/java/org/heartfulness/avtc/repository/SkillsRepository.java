package org.heartfulness.avtc.repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;
import org.heartfulness.avtc.model.Skill;

import java.util.List;

public interface SkillsRepository extends CrudRepository<Skill,Long> {
    //void save(Skill skill);
    List<Skill> findAll();
    @Query("select distinct s.skill from Skill s")
    List<String> getDistinctSkill();
}
