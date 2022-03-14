package com.silenteight.connector.ftcc.ingest.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.UUID;

@RequiredArgsConstructor
class MessageService {

  @NonNull
  private final MessageIdGenerator messageIdGenerator;
  @NonNull
  private final MessageRepository messageRepository;

  public UUID create(@NonNull UUID batchId, @NonNull JsonNode payload) {
    MessageEntity messageEntity = MessageEntity.builder()
        .batchId(batchId)
        .messageId(messageIdGenerator.generate())
        .payload(payload.asText())
        .build();
    return messageRepository.save(messageEntity).getMessageId();
  }
}
