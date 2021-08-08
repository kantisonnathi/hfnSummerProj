package org.heartfulness.avtc.controller;

import org.heartfulness.avtc.model.Caller;
import org.heartfulness.avtc.repository.CallerRepository;
import org.heartfulness.avtc.security.auth.models.User;
import org.heartfulness.avtc.service.AgentService;
import org.heartfulness.avtc.service.CallService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
@Transactional
public class CallerController {

    //TODO: let agent see all the callers he/she has talked to before
    //TODO: let team leader see all callers that team has spoken to
    //TODO: add call editing to the caller editing form

    private final CallService callService;
    private final AgentService agentService;
    private final CallerRepository callerRepository;

    public CallerController(CallService callService, AgentService agentService, CallerRepository callerRepository) {
        this.callService = callService;
        this.agentService = agentService;
        this.callerRepository = callerRepository;
    }


    @GetMapping("/callerDetails/{callerId}")
    public String getCallerEditForm(@PathVariable("callerId") Long callerId, ModelMap modelMap, @AuthenticationPrincipal User user) {
        Optional<Caller> caller = this.callerRepository.findById(callerId);
        modelMap.put("role", this.agentService.findBycontactNumber(user.getPhoneNumber()).getRole().toString());
        modelMap.put("caller", caller.get());
        return "main/callerDetails";
    }

    @PostMapping("/callerDetails/{callerId}")
    public String postCallerEditForm(Caller caller) {
        caller.setSaved(true);

        this.callerRepository.save(caller);
        return "redirect:/caller/" + caller.getId();
    }

    @GetMapping("/caller/{callerId}")
    public String viewCaller(@PathVariable("callerId") Long callerId, @AuthenticationPrincipal User user, ModelMap modelMap) {
        Caller caller = this.callerRepository.findById(callerId).get();
        modelMap.put("caller", caller);
        modelMap.put("role", this.agentService.findBycontactNumber(user.getPhoneNumber()).getRole().toString());
        return "caller/viewCaller";
    }
}
