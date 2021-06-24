package org.heartfulness.avtc.repository;

import org.heartfulness.avtc.model.Caller;
import org.springframework.data.repository.Repository;

public interface CallerRepository extends Repository<Caller, Long> {

    Caller save(Caller caller);

    Caller findById(Long id);

    Caller findByContactNumber(String contactNumber);

}
