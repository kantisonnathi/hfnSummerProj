package org.heartfulness.avtc.service;

import org.heartfulness.avtc.model.Agent;
import org.heartfulness.avtc.model.Call;
import org.heartfulness.avtc.model.Team;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CallService {

    List<Call> getAllCalls();

    void saveCall(Call call);

    Page<Call> findAllByAgent(Agent agent, int pageNo, int pageSize, String sortField, String sortDirection);

    Call findById(Long id);

    Page<Call> findPaginated(int pageno, int pagesize, String sortField, String sortDirection);

    Call findByUid(String uid);

    Page<Call> findAllByTeam(Team team, int pageNo, int pageSize, String sortField, String sortDirection);

}
