package org.heartfulness.avtc.repository;
import org.springframework.data.repository.Repository;
import org.heartfulness.avtc.model.Skill;

public interface SkillsRepository extends Repository<Skill,Long> {
    void save(Skill skill);
}
