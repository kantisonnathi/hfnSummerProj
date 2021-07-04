package org.heartfulness.avtc.repository;

import org.heartfulness.avtc.model.Caller;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CallerRepository extends JpaRepository<Caller, Long> {

    @NotNull
    Optional<Caller> findById(@NotNull Long id);

    Caller findByContactNumber(String contactNumber);

}
