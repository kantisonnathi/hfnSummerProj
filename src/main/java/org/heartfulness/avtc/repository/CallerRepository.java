package org.heartfulness.avtc.repository;

import org.heartfulness.avtc.model.Caller;
import org.springframework.data.repository.Repository;

public interface CallerRepository extends Repository<Caller, Long> {

    void save(Caller caller);

    Caller findByAllottedID(String callerID);

    Caller findByContactNumber(String contactNumber);

}
