package org.heartfulness.avtc.repository;

import org.heartfulness.avtc.model.Service;
import org.springframework.data.repository.Repository;

public interface ServiceRepository extends Repository<Service, Long> {

    Service findById(Integer integer);

}
