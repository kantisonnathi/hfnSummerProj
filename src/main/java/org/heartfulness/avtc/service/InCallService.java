package org.heartfulness.avtc.service;

import com.google.gson.Gson;
import org.heartfulness.avtc.model.*;
import org.heartfulness.avtc.model.enums.AgentStatus;
import org.heartfulness.avtc.model.enums.CallStatus;
import org.heartfulness.avtc.model.enums.LogEvent;
import org.heartfulness.avtc.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
public class InCallService {

    //TODO: rewrite missed call functionality
    //TODO: test missed call functionality
    //TODO: extract code into methods

    private final AgentRepository agentRepository;

    private final CallerRepository callerRepository;

    private final CallRepository callRepository;

    private final CallService callService;

    private final AgentService agentService;

    @Autowired
    public InCallService(AgentRepository agentRepository, CallerRepository callerRepository, CallRepository callRepository, CallService callService, AgentService agentService) {
        this.agentRepository = agentRepository;
        this.callerRepository = callerRepository;
        this.callRepository = callRepository;
        this.callService = callService;
        this.agentService = agentService;
    }

    public void inCall(String jsonString) {
        InCallNode inCallNode = processString(jsonString);
        Caller caller = this.callerRepository.findByContactNumber("+91" + inCallNode.getClid().strip());
        if (caller == null) {
            //increase the number of global callers.
            caller = new Caller();
            caller.setContactNumber("+91" + inCallNode.getClid());
            caller = this.callerRepository.save(caller);
        }
        Call call = this.callService.findByUid(inCallNode.getUid());
        Agent agent;
        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
        switch (inCallNode.getCall_state()) {
            case 1:
                //this means incoming call to server.
                //which means we need to create a call object and store it.
                call = new Call();
                call.setCaller(caller);
                call.setUid(inCallNode.getUid());
                caller.addCall(call);
                call.setStatus(CallStatus.CONNECTED_TO_IVR);
                this.callerRepository.save(caller);
                break;
            case 2:
                //call finished
                call.setStatus(CallStatus.DISCONNECTED);
                agent = call.getAgent();
                agent.setStatus(AgentStatus.ONLINE); //the logger should not be updated here
                agent.setTimestamp(timestamp);
                call.setEndTime(inCallNode.getCall_time());
                this.agentRepository.save(agent);
                call = this.callRepository.save(call);
                break;
            case 5:
                //dialing agents
                agent = call.getAgent();
                agent.setStatus(AgentStatus.DIALING);
                agent.setLeasedBy(call);
                this.agentRepository.save(agent);
                break;
            case 6:
                // agent picked up
                String fullUser = inCallNode.getUsers().get(0);
                String phoneNumber = fullUser.substring(fullUser.length() - 10, fullUser.length());
                call.setStatus(CallStatus.CONNECTED_TO_AGENT);
                agent = this.agentService.findBycontactNumber("+91" + phoneNumber);
                if (agent == null) {
                    System.out.println("Agent is null, state 6\n\n\n\n");
                    break;
                }
                call.setAgent(agent);
                agent.setStatus(AgentStatus.IN_CALL);
                Set<Agent> leasedAgents = call.getLeasing();
                call.setLeasing(null);
                for (Agent currAgent : leasedAgents) {
                    currAgent.setLeasedBy(null);
                    currAgent.setStatus(AgentStatus.ONLINE);
                    this.agentRepository.save(currAgent);
                }
                List<Agent> missedAgents = this.agentRepository.findAllByLeasedByAndStatus(call, AgentStatus.DIALING);
                for (Agent currAgent : missedAgents) {
                    currAgent.setLeasedBy(null);
                    currAgent.addMissed();
                    if (currAgent.getMissed() >= 3) {
                        currAgent.setLeasedBy(null);
                        currAgent.setStatus(AgentStatus.OFFLINE);
                        currAgent.setTimestamp(timestamp);
                        currAgent.setMissed(0);
                        Logger log = new Logger();
                        log.setAgent(currAgent);
                        log.setLogEvent(LogEvent.FORCED_OFFLINE);
                    } else {
                        currAgent.setStatus(AgentStatus.ONLINE);
                        currAgent.setTimestamp(timestamp);
                    }
                    this.agentRepository.save(agent);
                }
                this.callRepository.save(call);
                break;
            default:
                System.out.println("rip");

        }
    }

    public InCallNode processString(String jsonString) {
        System.out.println("\n\n\n" + jsonString + "\n\n\n");

        for (int i = 0; i < jsonString.length(); i++) {
            if (jsonString.charAt(i) == '[' && jsonString.charAt(i-1) == '\"') {
                jsonString = jsonString.substring(0,i-1) + jsonString.substring(i);
            }
            if (jsonString.charAt(i) == ']' && jsonString.charAt(i+1) == '\"') {
                jsonString = jsonString.substring(0,i+1) + jsonString.substring(i+2);
            }
            if (jsonString.charAt(i) == '\\') {
                jsonString = jsonString.substring(0,i) + jsonString.substring(i+1);
            }
        }
        Gson gson = new Gson();
        return gson.fromJson(jsonString, InCallNode.class);
    }
}
