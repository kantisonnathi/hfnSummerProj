package org.heartfulness.avtc.model;

import org.heartfulness.avtc.repository.AgentRepository;
import org.heartfulness.avtc.repository.SkillsRepository;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SkillFormat implements Converter<String,Skill> {
    private SkillsRepository skillsRepository;
    private AgentRepository agentRepository;

    public SkillFormat(SkillsRepository skillsRepository, AgentRepository agentRepository) {
        this.skillsRepository = skillsRepository;
        this.agentRepository = agentRepository;
    }

    @Override
    public Skill convert(String s) {
        System.out.println("Skill");
        int id=Integer.parseInt(s);
        List<Skill> skills=skillsRepository.findAll();
        int index=id-1;
        return skills.get(index);
    }
}
