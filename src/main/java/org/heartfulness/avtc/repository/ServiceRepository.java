package org.heartfulness.avtc.repository;

import org.heartfulness.avtc.model.Service;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;

import java.util.List;


public interface ServiceRepository extends CrudRepository<Service, Long> {

    Service findById(Integer integer);
    List<Service> findAll();

}
