package org.heartfulness.avtc.controller;

import org.heartfulness.avtc.model.*;
import org.heartfulness.avtc.repository.AgentRepository;
import org.heartfulness.avtc.repository.DepartmentRepository;
import org.heartfulness.avtc.repository.LanguageRepository;
import org.heartfulness.avtc.repository.ServiceRepository;
import org.heartfulness.avtc.security.auth.SecurityService;
import org.heartfulness.avtc.security.auth.models.User;
import org.heartfulness.avtc.service.AgentService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;


@Controller
public class DepartmentController {

    private final SecurityService securityService;
    private final AgentService agentService;
    private final LanguageRepository languageRepository;
    private final ServiceRepository serviceRepository;
    private final AgentRepository agentRepository;
    private final DepartmentRepository departmentRepository;

    public DepartmentController(SecurityService securityService, AgentService agentService, LanguageRepository languageRepository, ServiceRepository serviceRepository, AgentRepository agentRepository, DepartmentRepository departmentRepository) {
        this.securityService = securityService;
        this.agentService = agentService;
        this.languageRepository = languageRepository;
        this.serviceRepository = serviceRepository;
        this.agentRepository = agentRepository;
        this.departmentRepository = departmentRepository;
    }

    @GetMapping("/addDetails")
    public String getDetails(ModelMap modelMap) {
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
    }

    @PostMapping("/addDetails")
    public String afterDetails(@ModelAttribute("agent") Agent agent, RedirectAttributes redirectAttributes) {
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
        redirectAttributes.addFlashAttribute("message", "Your changes have been saved!");
        return "redirect:/success";
    }


    @GetMapping("/addLangandServ")
    public String addLanguage(ModelMap modelMap, @AuthenticationPrincipal User user) {
        Agent agent = this.agentService.findBycontactNumber(user.getPhoneNumber());
        Department department=new Department();
        modelMap.put("department",department);
        modelMap.put("role", agent.getRole().toString());
        return "main/addLang";
    }


    @PostMapping("/addLangandServ")
    public String newDept(@ModelAttribute("department") Department department) {
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
        return "redirect:/success";
    }
}
