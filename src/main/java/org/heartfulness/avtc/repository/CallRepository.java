package org.heartfulness.avtc.repository;

import org.heartfulness.avtc.model.Agent;
import org.heartfulness.avtc.model.Call;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

public interface CallRepository extends JpaRepository<Call, Long> {

    Page<Call> findAllPaginatedByAgent(Agent agent, Pageable pageable);

    List<Call> findAllByAgent(Agent agent);

    @NotNull
    Optional<Call> findById(@NotNull Long id);

    Optional<Call> findByUid(String uid);

    Integer countAllByDate(Date date);

}
