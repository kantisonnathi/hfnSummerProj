package org.heartfulness.avtc.service;

import org.heartfulness.avtc.model.Agent;
import org.heartfulness.avtc.model.Call;
import org.heartfulness.avtc.repository.CallRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CallServiceImpl implements CallService {

    @Autowired
    private CallRepository callRepository;

    @Override
    public List<Call> getAllCalls() {
        return this.callRepository.findAll();
    }

    @Override
    public Call findById(Long id) {
        Optional<Call> optional = this.callRepository.findById(id);
        Call call = null;
        if (optional.isPresent()) {
            call = optional.get();
        } else {
            throw new RuntimeException("call not found for id :: " + id);
        }
        return call;
    }

    @Override
    public void saveCall(Call call) {
        this.callRepository.save(call);
    }

    @Override
    public Page<Call> findAllByAgent(Agent agent, int pageNo, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return this.callRepository.findAllByAgent(agent, pageable);
    }

    @Override
    public Page<Call> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return this.callRepository.findAll(pageable);
    }

    @Override
    public Call findByUid(String uid) {
        Optional<Call> call = this.callRepository.findByUid(uid);
        return call.orElse(null);
    }

}
