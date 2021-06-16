package org.heartfulness.avtc.repository;

import org.heartfulness.avtc.model.Service;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;


public interface ServiceRepository extends CrudRepository<Service, Long> {

    Optional<Service> findById(Long lon);
    List<Service> findAll();

}
