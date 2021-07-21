package org.heartfulness.avtc.controller;

import org.heartfulness.avtc.model.Agent;
import org.heartfulness.avtc.model.Call;
import org.heartfulness.avtc.service.AgentService;
import org.heartfulness.avtc.service.CallService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class CallController {

    private final CallService callService;
    private final AgentService agentService;

    public CallController(CallService callService, AgentService agentService) {
        this.callService = callService;
        this.agentService = agentService;
    }

    @GetMapping("/calls/all")
    public String getAllCalls(ModelMap modelMap) {
        return findPaginated(1, "id", "asc", modelMap);
    }

    @GetMapping("/page/{pageNo}")
    public String findPaginated(@PathVariable("pageNo") Integer pageNo,
                                @RequestParam("sortField") String sortField,
                                @RequestParam("sortDir") String sortDir, ModelMap modelMap) {
        int pageSize = 10;

        Page<Call> page = callService.findPaginated(pageNo, pageSize, sortField, sortDir);
        List<Call> listCalls = page.getContent();
        modelMap.put("url", "/page/");

        modelMap.put("currentPage", pageNo);
        modelMap.put("totalPages", page.getTotalPages());
        modelMap.put("totalItems", page.getTotalElements());
        modelMap.put("sortField", sortField);
        modelMap.put("sortDir", sortDir);
        modelMap.put("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        modelMap.put("listCalls", listCalls);
        return "calls/viewCalls";
    }


}
