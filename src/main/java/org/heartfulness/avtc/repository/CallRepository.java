package org.heartfulness.avtc.repository;

import org.heartfulness.avtc.model.Agent;
import org.heartfulness.avtc.model.Call;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface CallRepository extends Repository<Call, Long> {

    List<Call> findAllByAgent(Agent agent);

}
