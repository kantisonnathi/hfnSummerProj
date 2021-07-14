package org.heartfulness.avtc.controller;

import org.heartfulness.avtc.model.Call;
import org.heartfulness.avtc.model.Caller;
import org.heartfulness.avtc.repository.CallerRepository;
import org.heartfulness.avtc.service.CallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.ws.rs.Path;
import java.util.List;
import java.util.Optional;

@Controller
public class CallerController {

    //TODO: add all autowired attributes to constructor.
    //TODO: let agent see all the callers he/she has talked to before
    //TODO: let team leader see all callers that team has spoken to

    @Autowired
    private CallService callService;

    private final CallerRepository callerRepository;

    public CallerController(CallerRepository callerRepository) {
        this.callerRepository = callerRepository;
    }

    @GetMapping("/callerDetails/{callerId}")
    public String getCallerEditForm(@PathVariable("callerId") Long callerId, ModelMap modelMap) {
        Optional<Caller> caller = this.callerRepository.findById(callerId);
        modelMap.put("caller", caller.get());
        return "main/callerDetails";
    }

    @PostMapping("/callerDetails/{callerId}")
    public String postCallerEditForm(Caller caller) {
        caller.setSaved(true);

        this.callerRepository.save(caller);
        return "redirect:/success";
    }
}
