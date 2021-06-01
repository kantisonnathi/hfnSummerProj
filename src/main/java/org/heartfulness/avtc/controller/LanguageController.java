package org.heartfulness.avtc.controller;

import org.heartfulness.avtc.model.Agent;
import org.heartfulness.avtc.model.Language;
//import org.heartfulness.avtc.model.Skill;
import org.heartfulness.avtc.repository.AgentRepository;
import org.heartfulness.avtc.repository.LanguageRepository;
import org.heartfulness.avtc.repository.SkillsRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Controller
public class LanguageController {
    private final AgentRepository agentRepository;
    private final LanguageRepository languageRepository;
    private final SkillsRepository skillsRepository;

    public LanguageController(AgentRepository agentRepository, LanguageRepository languageRepository, SkillsRepository skillsRepository) {
        this.agentRepository = agentRepository;
        this.languageRepository = languageRepository;
        this.skillsRepository = skillsRepository;
    }

    @GetMapping("/addDetails")
    public String getDetails(ModelMap modelMap)
    {
        List<Language> l=new ArrayList<>();
        List<String> skills=new ArrayList<>();
        Agent agent= agentRepository.findByContactNumber("+919550563765");
        l=languageRepository.findByAgentId(agent.getId());
        modelMap.put("agent",agent);
        modelMap.put("l",l);
        return "main/NewDetails";
    }
    @PostMapping("/addDetails")
    public String getSuccessPage(@ModelAttribute("agent") Agent agen)
    {
        Agent agent=agentRepository.findByContactNumber("+919550563765"); //for testing

        Language language=new Language();
        language.setAgent(agent);
        language.setId(agent.getId());
        for ( Language l: agen.getLanguages()) {
           l.setId(agent.getId());
            languageRepository.save(l); //Test saving the language prefernce

        }
    /*    Skill skill=new Skill();    //Write similar code for Skill
        skill.setId(agent.getId());
        skill.setAgent(agent);
        for(String s: skills)
        {
            skill.setSkill(s);
            skillsRepository.save(skill);
        }*/
        return "main/success";
    }
}