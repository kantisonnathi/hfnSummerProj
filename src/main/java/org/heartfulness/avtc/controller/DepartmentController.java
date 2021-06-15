package org.heartfulness.avtc.controller;

import org.heartfulness.avtc.model.*;
import org.heartfulness.avtc.repository.AgentRepository;
import org.heartfulness.avtc.repository.DepartmentRepository;
import org.heartfulness.avtc.repository.LanguageRepository;
import org.heartfulness.avtc.repository.ServiceRepository;
import org.heartfulness.avtc.security.auth.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;


@Controller
public class DepartmentController {
    @Autowired
    SecurityService securityService;

    private final LanguageRepository languageRepository;
    private final ServiceRepository serviceRepository;
    private final AgentRepository agentRepository;
    private final DepartmentRepository departmentRepository;

    public DepartmentController(LanguageRepository languageRepository, ServiceRepository serviceRepository, AgentRepository agentRepository, DepartmentRepository departmentRepository) {
        this.languageRepository = languageRepository;
        this.serviceRepository = serviceRepository;
        this.agentRepository = agentRepository;
        this.departmentRepository = departmentRepository;
    }

    @GetMapping("/addDetails")
    public String getDetails(ModelMap modelMap)
    {
        List<Language> languages=languageRepository.findAll();
        List<Service> services=serviceRepository.findAll();
        Department department=new Department();
        List<Language> entered=new ArrayList<>();
       Other other=new Other();
        Agent agent=agentRepository.findByContactNumber(securityService.getUser().getPhoneNumber());
        modelMap.put("agent",agent);
        modelMap.put("languages",languages);
        modelMap.put("services",services);
        modelMap.put("other",other);
        return "main/NewDetails";
    }
    @PostMapping("/addDetails")
    public String afterDetails(@ModelAttribute("agent") Agent agent,@ModelAttribute("other") Other other)
    {
         Agent newAgent=agentRepository.findByContactNumber(securityService.getUser().getPhoneNumber());
         newAgent.setGender(agent.getGender());
         newAgent.setName(agent.getName());
         Department department=new Department();
         for(Language language: other.getLanguages())
         {
             department=departmentRepository.findByServiceAndLanguage(other.getServices().get(0),language);
             //Write code to save the agent id and dept id ti agent_department table
         }
         agentRepository.save(newAgent);
        // return "redirect:/success"; not working?
        return "main/success";
    }
}
