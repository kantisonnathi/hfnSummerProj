package org.heartfulness.avtc.controller;

import org.heartfulness.avtc.model.*;
import org.heartfulness.avtc.repository.AgentRepository;
import org.heartfulness.avtc.repository.DepartmentRepository;
import org.heartfulness.avtc.repository.LanguageRepository;
import org.heartfulness.avtc.repository.ServiceRepository;
import org.heartfulness.avtc.security.auth.SecurityService;
import org.heartfulness.avtc.service.AgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Controller
public class DepartmentController {
    @Autowired
    SecurityService securityService;
    @Autowired
    AgentService agentService;

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
       List<Department> departments=departmentRepository.findAll();
       List<Other> others=new ArrayList<>();
       for(Department d: departments)
       {
           Language lang=d.getLanguage();
           Service service=d.getService();
           Other other=new Other();
           other.setLanguages(lang.getName());
           other.setServices(service.getName());
           other.setId(d.getId());
           others.add(other);
       }
        List<Language> entered=new ArrayList<>();
        Agent agent=agentService.findBycontactNumber(securityService.getUser().getPhoneNumber());

        modelMap.put("agent",agent);
        modelMap.put("others",others);
        return "main/NewDetails";
    } //GetMapping working for dept


    @PostMapping("/addDetails")
    public String afterDetails(@ModelAttribute("agent") Agent agent)
    {
         Agent newAgent= agentService.findBycontactNumber(securityService.getUser().getPhoneNumber());
         newAgent.setGender(agent.getGender());
         newAgent.setName(agent.getName());
         newAgent.setDepartments(agent.getDepartments());
         Department department=new Department();
       /*  for(Language language: other.getLanguages())
         {
             department=departmentRepository.findByServiceAndLanguage(other.getServices().get(0),language);
             //Write code to save the agent id and dept id ti agent_department table
         }*/
         agentRepository.save(newAgent);
        // return "redirect:/success"; not working?
        return "main/success";
    }


    @GetMapping("/addLangandServ")
    public String addLanguage(ModelMap modelMap)
    {
        Department department=new Department();
        modelMap.put("department",department);
        return "main/addLang";
    }


    @PostMapping("/addLangandServ")
    public String newDept(@ModelAttribute("department") Department department)
    {
        if(!department.getService().getName().equals(""))
        {
            serviceRepository.save(department.getService());
        }
        if(!department.getLanguage().getName().equals("")) {
            languageRepository.save(department.getLanguage());
            List<Service> services = serviceRepository.findAll();
            for (Service s : services) {
                Department d = new Department();
                d.setLanguage(department.getLanguage());
                d.setService(s);
                departmentRepository.save(d);
            }
        }
        if(!department.getService().getName().equals(""))
        {
            List<Language> languages=languageRepository.findAll();
            for(Language l: languages)
            {
                Department d=new Department();
                d.setLanguage(l);
                d.setService(department.getService());
                departmentRepository.save(d);
            }
        }
        return "main/mainpage";// change mappings
    }
}
