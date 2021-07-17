package org.heartfulness.avtc.service;

import net.minidev.json.JSONObject;
import org.heartfulness.avtc.Comparator.SortByTimeStamp;
import org.heartfulness.avtc.config.NodeConfiguration;
import org.heartfulness.avtc.model.*;
import org.heartfulness.avtc.model.enums.AgentStatus;
import org.heartfulness.avtc.model.enums.CallStatus;
import org.heartfulness.avtc.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class InputNodeService {

    private final NodeConfiguration nodeConfiguration;

    private final AgentRepository agentRepository;

    private final CallerRepository callerRepository;

    private final LanguageRepository languageRepository;

    private final ServiceRepository serviceRepository;

    private final CallRepository callRepository;

    private final DepartmentRepository departmentRepository;

    private final CallService callService;

    private final AgentService agentService;

    @Autowired
    public InputNodeService(NodeConfiguration nodeConfiguration, AgentRepository agentRepository, CallerRepository callerRepository, LanguageRepository languageRepository, ServiceRepository serviceRepository, CallRepository callRepository, DepartmentRepository departmentRepository, CallService callService, AgentService agentService) {
        this.nodeConfiguration = nodeConfiguration;
        this.agentRepository = agentRepository;
        this.callerRepository = callerRepository;
        this.languageRepository = languageRepository;
        this.serviceRepository = serviceRepository;
        this.callRepository = callRepository;
        this.departmentRepository = departmentRepository;
        this.callService = callService;
        this.agentService = agentService;
    }

    public ResponseEntity<?> input(@RequestBody InputNode input) {
        System.out.println("Data from my operator: " + input.toString());
        Caller caller = this.callerRepository.findByContactNumber("+91" + input.getClid());
        if (caller == null) {
            caller = new Caller();
            input.setClid("+91" + input.getClid());
            caller.setContactNumber(input.getClid());
            this.callerRepository.save(caller);
        }
        String currNodeID = input.getNode_id();
        Language currLanguage = null;
        if (currNodeID.equals(nodeConfiguration.getEnglishNode())) {
            currLanguage = this.languageRepository.findByName("English");
        } else if (currNodeID.equals(nodeConfiguration.getHindiNode())) {
            currLanguage = this.languageRepository.findByName("Hindi");
        }
        Optional<org.heartfulness.avtc.model.Service> currService = this.serviceRepository.findById(Long.valueOf(input.getInput()));
        Department currentDepartment = this.departmentRepository.findByServiceAndLanguage(currService.get(), currLanguage);
        List<String> number = new ArrayList<>();
        Long x = currentDepartment.getId();
        List<Agent> agents = new ArrayList<>();
        /*List<Call> calls = this.callRepository.findAllByCallerAndCallStatus(caller, CallStatus.CONNECTED_TO_IVR);
        Call call = calls.get(calls.size()-1);*/
        Call call = this.callService.findByUid(input.getUid());
        int i =caller.getLevel();
        agents=agentRepository.getByStatusandDepartment(x,i);
        agents.sort(new SortByTimeStamp());

        for (int j = 0; j < 3&& j<agents.size(); j++) {
            Agent tempAgent = agents.get(j);
            tempAgent.setStatus(AgentStatus.QUEUED);
            tempAgent.setLeasedBy(call);
            this.agentRepository.save(tempAgent);
            number.add(agents.get(j).getContactNumber());
        }

        while (number.size() < 3 && i< 3)//i represents level
        {
            List<Agent> agentsnext=agentRepository.getByStatusandDepartment(x,++i);
            agentsnext.sort(new SortByTimeStamp());
            for(int j=0;j<3-number.size()&&j<agentsnext.size();j++)
            {
                Agent tempAgent = agents.get(j);
                tempAgent.setStatus(AgentStatus.QUEUED);
                tempAgent.setLeasedBy(call);
                this.agentRepository.save(tempAgent);
                number.add(agentsnext.get(j).getContactNumber());
            }
        }
        if (agents.isEmpty()) {
            JSONObject entity = new JSONObject();
            call.setStatus(CallStatus.DISCONNECTED);
            this.callRepository.save(call);
            entity.put("action", "tts");
            entity.put("value", "Sorry, there are no community level workers available at this time, please try again " +
                    "later");
            return new ResponseEntity<>(entity, HttpStatus.OK);
        }
        if (agents.size() < 3) {
            for (int j = 0; j < agents.size(); j++) {
                Agent tempAgent = agents.get(j);
                tempAgent.setStatus(AgentStatus.QUEUED);
                tempAgent.setLeasedBy(call);
                this.agentRepository.save(tempAgent);
                number.add(agents.get(j).getContactNumber());
            }
        }

        call.setStatus(CallStatus.AWAITING_CONNECTION_TO_AGENT);
        this.callRepository.save(call);
        JSONObject entity = new JSONObject();
        entity.put("operation", "dial-numbers");
        JSONObject operationData = new JSONObject();
        operationData.put("data",number);
        operationData.put("dial_method", "serial");
        operationData.put("anon_uuid","60cd93e244d59476");
        entity.put("operation_data",operationData);
        return new ResponseEntity<Object>(entity, HttpStatus.OK);

    }
}
