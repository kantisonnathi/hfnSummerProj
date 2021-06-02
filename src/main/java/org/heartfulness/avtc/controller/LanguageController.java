package org.heartfulness.avtc.controller;

import com.google.firebase.auth.FirebaseAuth;
import org.heartfulness.avtc.Firebase.FirebaseInitializer;
import org.heartfulness.avtc.model.Agent;
import org.heartfulness.avtc.model.Language;
//import org.heartfulness.avtc.model.Skill;
import org.heartfulness.avtc.model.Other;
import org.heartfulness.avtc.model.Skill;
import org.heartfulness.avtc.repository.AgentRepository;
import org.heartfulness.avtc.repository.LanguageRepository;
import org.heartfulness.avtc.repository.SkillsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class LanguageController {


    private final AgentRepository agentRepository;
    private final LanguageRepository languageRepository;
    private final SkillsRepository skillsRepository;
    @Autowired
    FirebaseInitializer fbi;

    public LanguageController(AgentRepository agentRepository, LanguageRepository languageRepository, SkillsRepository skillsRepository) {
        this.agentRepository = agentRepository;
        this.languageRepository = languageRepository;
        this.skillsRepository = skillsRepository;
    }

    @GetMapping("/addDetails")
    public String getDetails(ModelMap modelMap)
    {
        List<Language> l=new ArrayList<>();
        List<Skill> skills=new ArrayList<>();
        Other other=new Other();
        String lang= "";
        Agent agent= agentRepository.findByContactNumber("+919550563765");
        l=languageRepository.findByAgentId(agent.getId());
        skills=skillsRepository.findAll();
        modelMap.put("agent",agent);
        modelMap.put("l",l);
        modelMap.put("other",other);
        modelMap.put("skills",skills);
        FirebaseAuth fba=fbi.getClient();
        System.out.println(fba);
        return "main/NewDetails";
    }
    @PostMapping("/addDetails")
    public String getSuccessPage(@ModelAttribute("agent") Agent agen,@ModelAttribute("other") Other other)
    {
        Agent agent=agentRepository.findByContactNumber("+9676684424"); //for testing


        String others[]=other.getLan().split(",");
        for ( Language l: agen.getLanguages()) {
            Language language=new Language();
            language.setAgent(agent);
            language.setLanguage(l.getLanguage());
        Language catchh= this.languageRepository.save(language); //Test saving the language preference
        }
        for(String s: others)
        {
            Language language=new Language();
            language.setAgent(agent);
            language.setLanguage(s);
            Language catchh=this.languageRepository.save(language);
        }

        String otherskills[]=other.getSkills1().split(",");

        for(Skill s: agen.getSkills())
        {
            Skill skill=new Skill();
            skill.setAgent(agent);
            skill.setSkill(s.getSkill());
            this.skillsRepository.save(skill);
        }
        for(String s: otherskills)
        {
            Skill skill=new Skill();
            skill.setAgent(agent);
            skill.setSkill(s);
            this.skillsRepository.save(skill);
        }
        return "main/success";
    }
}