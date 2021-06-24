package org.heartfulness.avtc.repository;

import org.heartfulness.avtc.model.Agent;
import org.heartfulness.avtc.model.Call;
import org.heartfulness.avtc.model.CallStatus;
import org.heartfulness.avtc.model.Caller;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Set;

public interface CallRepository extends Repository<Call, Long> {

    List<Call> findAllByAgent(Agent agent);

    Call save(Call call);

    Call findByCallerAndStartTime(Caller caller, String startTime);
    Call findById(Long id);
    Call findByCaller(Caller caller);

    Set<Call> findAllByCallerAndAgent(Caller caller, Agent agent);

    Call findByCallerAndCallStatus(Caller caller, CallStatus callStatus);

    List<Call> findAllByCallerAndCallStatus(Caller caller, CallStatus callStatus);

}
