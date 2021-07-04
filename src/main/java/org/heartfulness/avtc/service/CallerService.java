package org.heartfulness.avtc.service;

import org.heartfulness.avtc.model.Caller;
import org.springframework.data.domain.Page;

public interface CallerService {

    Page<Caller> findAllPaginatedCallers(int pageNo, int pageSize, String sortField, String sortDirection);

}
