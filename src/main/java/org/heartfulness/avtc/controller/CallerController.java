package org.heartfulness.avtc.controller;

import org.heartfulness.avtc.model.Caller;
import org.heartfulness.avtc.repository.CallerRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.ws.rs.Path;

@Controller
public class CallerController {

    private final CallerRepository callerRepository;

    public CallerController(CallerRepository callerRepository) {
        this.callerRepository = callerRepository;
    }

    @GetMapping("/callerDetails/{callerId}")
    public String getCallerEditForm(@PathVariable("callerId") Long callerId, ModelMap modelMap) {
        Caller caller = this.callerRepository.findById(callerId);
        modelMap.put("caller", caller);
        return "main/callerDetails";
    }

    @PostMapping("/callerDetails/{callerId}")
    public String postCallerEditForm(Caller caller) {
        caller.setSaved(true);

        this.callerRepository.save(caller);
        return "redirect:/success";
    }
}
