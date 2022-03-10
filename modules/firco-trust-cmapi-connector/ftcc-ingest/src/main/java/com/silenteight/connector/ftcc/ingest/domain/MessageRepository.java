package com.silenteight.connector.ftcc.ingest.domain;

import org.springframework.data.repository.Repository;

import java.util.Optional;
import java.util.UUID;

interface MessageRepository extends Repository<MessageEntity, Long> {

  MessageEntity save(MessageEntity messageEntity);

  Optional<MessageEntity> findByBatchId(UUID batchId);
}
