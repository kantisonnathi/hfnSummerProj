package org.heartfulness.avtc.controller;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import org.heartfulness.avtc.firebase.FirebaseInitializer;
import org.heartfulness.avtc.model.Agent;
import org.heartfulness.avtc.model.Language;
//import org.heartfulness.avtc.model.Skill;
import org.heartfulness.avtc.model.Other;
import org.heartfulness.avtc.model.Skill;
import org.heartfulness.avtc.repository.AgentRepository;
import org.heartfulness.avtc.repository.LanguageRepository;
import org.heartfulness.avtc.repository.SkillsRepository;
import org.heartfulness.avtc.security.auth.SecurityService;
import org.heartfulness.avtc.security.auth.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Controller
public class LanguageController {


    private final AgentRepository agentRepository;
    private final LanguageRepository languageRepository;
    private final SkillsRepository skillsRepository;

    @Autowired
    FirebaseInitializer fbi;

    @Autowired
    SecurityService securityService;

    public LanguageController(AgentRepository agentRepository, LanguageRepository languageRepository, SkillsRepository skillsRepository) {
        this.agentRepository = agentRepository;
        this.languageRepository = languageRepository;
        this.skillsRepository = skillsRepository;
    }

    @GetMapping("/addDetails")
    public String getDetails(ModelMap modelMap) throws FirebaseAuthException {
        List<String> l1=languageRepository.findDistinctLanguage();

     List<Language> l=new ArrayList<Language>();
     List<Language> all=languageRepository.findAll();
        for(String s: l1)
        {
            for(Language l2: all)
            {
                if(s.equals(l2.getLanguage()))
                {
                    Language lang=new Language();
                    lang.setLanguage(s);
                    lang.setId(l2.getId());
                    l.add(lang);
                    break;
                }
            }
        }
        List<String> s1=skillsRepository.getDistinctSkill();

        List<Skill> skills=new ArrayList<Skill>();
        List<Skill> total=skillsRepository.findAll();
        for(String s: s1)
        {
            for(Skill s2: total)
            {
                if(s.equals(s2.getSkill()))
                {
                    Skill skill=new Skill();
                    skill.setSkill(s);
                    skill.setId(s2.getId());
                    skills.add(skill);
                    break;
                }
            }
        }
       // List<Skill> skills=new ArrayList<>();
        Other other=new Other();
        User user=securityService.getUser();
        Agent agent= agentRepository.findByContactNumber(user.getPhoneNumber());
       // skills=skillsRepository.findAll();
        modelMap.put("agent",agent);
        modelMap.put("l",l);
        modelMap.put("other",other);
        modelMap.put("skills",skills);
      //UserRecord.CreateRequest newUser = new UserRecord.CreateRequest();
        //UserRecord user = FirebaseAuth.getInstance().createUser(newUser);
        return "main/NewDetails";
    }
    @PostMapping("/addDetails")
    public String getSuccessPage(@ModelAttribute("agent") Agent agen,@ModelAttribute("other") Other other)
    {
        User user = securityService.getUser();
        String number=user.getPhoneNumber();
        Agent agent=agentRepository.findByContactNumber(number); //for testing


        String others[]=other.getLan().split(",");
        for ( Language l: agen.getLanguages()) {
            Language language=new Language();
            language.setAgent(agent);
            language.setLanguage(l.getLanguage());
        Language catchh= this.languageRepository.save(language); //Test saving the language preference
        }
        for(String s: others)
        {
            if(s=="")
            {
                continue;
            }
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
            if(s=="")
            {
                continue;
            }
            Skill skill=new Skill();
            skill.setAgent(agent);
            skill.setSkill(s);
            this.skillsRepository.save(skill);
        }
        return "main/success";
    }
}