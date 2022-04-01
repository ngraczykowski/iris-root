package com.silenteight.connector.ftcc.callback.handler;

import com.silenteight.connector.ftcc.callback.handler.BatchCompletedEntity.BatchCompletedId;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BatchCompletedRepository
    extends JpaRepository<BatchCompletedEntity, BatchCompletedId> {
}
