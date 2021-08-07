package org.heartfulness.avtc.security.auth.models;

import org.heartfulness.avtc.model.Agent;
import org.heartfulness.avtc.repository.AgentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public class CustomAgentDetailsService implements UserDetailsService {

    @Autowired
    private AgentRepository agentRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Agent> agent = agentRepository.findByContactNumber(username);
        if (agent.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        return new CustomAgentDetails(agent.get());
    }
}
