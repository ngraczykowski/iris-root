package com.silenteight.connector.ftcc.ingest.domain;

import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.UUID;

interface MessageRepository extends Repository<MessageEntity, Long> {

  MessageEntity save(MessageEntity messageEntity);

  List<MessageEntity> findByBatchId(UUID batchId);
}
