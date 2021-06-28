package org.heartfulness.avtc.model;

import org.heartfulness.avtc.repository.AgentRepository;
import org.heartfulness.avtc.repository.LanguageRepository;
import org.heartfulness.avtc.security.auth.SecurityService;
import org.heartfulness.avtc.service.AgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;


import javax.print.attribute.HashAttributeSet;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Component
public class LangFormat implements Converter<String,Language> {
    @Autowired
    SecurityService securityService;
    @Autowired
    AgentService agentService;
    private LanguageRepository languageRepository;
    private AgentRepository agentRepository;

    public LangFormat(LanguageRepository languageRepository, AgentRepository agentRepository) {
        this.languageRepository = languageRepository;
        this.agentRepository = agentRepository;
    }

    @Override
    public Language convert(String id) {
        //System.out.println("Trying to convert "+ id+ "into a lang");
        int parsedId=Integer.parseInt(id);
        Agent agent= agentService.findBycontactNumber(securityService.getUser().getPhoneNumber());
        List<Language> l=languageRepository.findAll();
        int index=parsedId-1;
        return l.get(index);

    }
}
