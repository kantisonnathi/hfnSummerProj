package org.heartfulness.avtc.repository;

import org.heartfulness.avtc.model.Logger;
import org.springframework.data.repository.Repository;

public interface LoggerRepository extends Repository<Logger,Long> {
    public void save(Logger logger);
}
