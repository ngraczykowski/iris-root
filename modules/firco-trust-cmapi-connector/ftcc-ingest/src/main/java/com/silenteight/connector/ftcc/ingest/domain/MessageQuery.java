package com.silenteight.connector.ftcc.ingest.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.connector.ftcc.ingest.domain.dto.MessageDetailsDto;
import com.silenteight.connector.ftcc.ingest.domain.exception.MessageNotFoundException;

import java.util.UUID;

@RequiredArgsConstructor
class MessageQuery implements MessageDetailsQuery {

  @NonNull
  private final MessageRepository messageRepository;

  @Override
  public MessageDetailsDto details(@NonNull UUID batchId, @NonNull UUID messageId) {
    return messageRepository
        .findByBatchIdAndMessageId(batchId, messageId)
        .map(MessageEntity::toDetailsDto)
        .orElseThrow(() -> new MessageNotFoundException(batchId, messageId));
  }
}
