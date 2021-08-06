package org.heartfulness.avtc.service;

import com.google.gson.Gson;
import net.minidev.json.JSONObject;
import org.heartfulness.avtc.config.NodeConfiguration;
import org.heartfulness.avtc.model.AfterCallClasses.AfterCallNode;
import org.heartfulness.avtc.model.Agent;
import org.heartfulness.avtc.model.Call;
import org.heartfulness.avtc.model.Caller;
import org.heartfulness.avtc.model.enums.CallStatus;
import org.heartfulness.avtc.repository.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AfterCallService {

    private final NodeConfiguration nodeConfiguration;

    private final AgentRepository agentRepository;

    private final CallerRepository callerRepository;

    private final LanguageRepository languageRepository;

    private final ServiceRepository serviceRepository;

    private final CallRepository callRepository;

    private final MainRepository mainRepository;

    private final DepartmentRepository departmentRepository;

    private final CallService callService;

    private final AgentService agentService;

    public AfterCallService(NodeConfiguration nodeConfiguration, AgentRepository agentRepository, CallerRepository callerRepository, LanguageRepository languageRepository, ServiceRepository serviceRepository, CallRepository callRepository, MainRepository mainRepository, DepartmentRepository departmentRepository, CallService callService, AgentService agentService) {
        this.nodeConfiguration = nodeConfiguration;
        this.agentRepository = agentRepository;
        this.callerRepository = callerRepository;
        this.languageRepository = languageRepository;
        this.serviceRepository = serviceRepository;
        this.callRepository = callRepository;
        this.mainRepository = mainRepository;
        this.departmentRepository = departmentRepository;
        this.callService = callService;
        this.agentService = agentService;
    }

    public void after(String jsonString) {
        System.out.println("\n\n\n" + jsonString + "\n\n\n");

        List<JSONObject> entities = new ArrayList<>();
        Gson gson=new Gson();
        AfterCallNode afterCallNode=gson.fromJson(jsonString,AfterCallNode.class);
        Call call = this.callService.findByUid(afterCallNode.get_pm().get(0).getVl());
        //System.out.println(afterCallNode.get_cl());
        String a = afterCallNode.get_cl();
        /*Caller caller=callerRepository.findByContactNumber(a);
        call.setCaller(caller);*/ //caller is already set in the previous methods
        call.setLocation(afterCallNode.get_se());
        call.setUrl(afterCallNode.get_fu());
        call.setDuration(afterCallNode.get_dr());
        call.setStatus(CallStatus.DISCONNECTED);
        //Optional<Agent> agent = agentRepository.findById(call.getAgent().getId());
        /*if (agent.isPresent()) {
            call.setAgent(agent.get());
        } else {
            //missed call
            //get all the agents that are currently getting dialed, mark missed+1
        }*/
        //Agent agent=agentRepository.findByContactNumber("+917338897712");//Phone number was not caught during afternode testing

        callRepository.save(call);
    }
}
