package org.heartfulness.avtc.repository;

import org.heartfulness.avtc.model.Agent;
import org.heartfulness.avtc.model.Call;
import org.heartfulness.avtc.model.Caller;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface CallRepository extends Repository<Call, Long> {

    List<Call> findAllByAgent(Agent agent);

    void save(Call call);

    Call findByCallerAndStartTime(Caller caller, String startTime);

}
