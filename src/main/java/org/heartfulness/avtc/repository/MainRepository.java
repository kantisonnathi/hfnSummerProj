package org.heartfulness.avtc.repository;

import org.heartfulness.avtc.model.Main;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;

import java.sql.Date;

public interface MainRepository extends JpaRepository<Main, String> {

}
