package org.heartfulness.avtc.model;

import org.heartfulness.avtc.repository.AgentRepository;
import org.heartfulness.avtc.repository.ServiceRepository;
import org.heartfulness.avtc.security.auth.SecurityService;
import org.heartfulness.avtc.service.AgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Controller;

import java.util.List;
@Controller
public class ServiceFormat implements Converter<String,Service> {
    @Autowired
    SecurityService securityService;
    @Autowired
    AgentService agentService;
    private ServiceRepository serviceRepository;
    private AgentRepository agentRepository;

    public ServiceFormat(ServiceRepository serviceRepository, AgentRepository agentRepository) {
        this.serviceRepository = serviceRepository;
        this.agentRepository = agentRepository;
    }

    @Override
    public Service convert(String id) {
        //System.out.println("Trying to convert "+ id+ "into a lang");
        int parsedId=Integer.parseInt(id);
        Agent agent= agentService.findBycontactNumber(securityService.getUser().getPhoneNumber());
        List<Service> l=serviceRepository.findAll();
        int index=parsedId-1;
        return l.get(index);

    }
}
