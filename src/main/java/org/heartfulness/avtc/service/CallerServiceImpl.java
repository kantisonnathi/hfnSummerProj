package org.heartfulness.avtc.service;

import org.heartfulness.avtc.model.Caller;
import org.heartfulness.avtc.repository.CallRepository;
import org.heartfulness.avtc.repository.CallerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class CallerServiceImpl implements CallerService {

    @Autowired
    private CallerRepository callerRepository;

    @Override
    public Page<Caller> findAllPaginatedCallers(int pageNo, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return this.callerRepository.findAll(pageable);
    }

    @Override
    public Caller save(Caller caller) {
        return this.callerRepository.save(caller);
    }

    @Override
    public Caller findByID(Long id) {
        return this.callerRepository.findById(id).orElse(null);
    }

}
