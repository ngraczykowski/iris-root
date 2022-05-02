package com.silenteight.connector.ftcc.request.domain;

import org.springframework.data.repository.Repository;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

interface MessageRepository extends Repository<MessageEntity, Long> {

  MessageEntity save(MessageEntity messageEntity);

  Collection<MessageEntity> findAllByBatchId(UUID batchId);

  Optional<MessageEntity> findByBatchIdAndMessageIdAndCreatedAtAfter(
      UUID batchId, UUID messageId, OffsetDateTime createdAt);
}
