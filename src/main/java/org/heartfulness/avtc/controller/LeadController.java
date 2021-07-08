package org.heartfulness.avtc.controller;

import org.heartfulness.avtc.service.AgentService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/lead")
public class LeadController {

    private AgentService agentService;



}
