package org.heartfulness.avtc.repository;

import org.heartfulness.avtc.model.Agent;
import org.heartfulness.avtc.model.Department;
import org.heartfulness.avtc.model.Language;
import org.heartfulness.avtc.model.Service;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface DepartmentRepository extends CrudRepository<Department, Long> {

    Department findByServiceAndLanguage(Service service, Language language);
    List<Department> findAll();
}
