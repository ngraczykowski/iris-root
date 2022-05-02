package com.silenteight.connector.ftcc.request.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.connector.ftcc.common.database.partition.PartitionCreator;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.UUID;
import javax.annotation.PostConstruct;

import static java.time.OffsetDateTime.now;

@RequiredArgsConstructor
public class MessageService {

  private static final String TABLE_NAME = "ftcc_message";

  @NonNull
  private final MessageIdGenerator messageIdGenerator;
  @NonNull
  private final MessageRepository messageRepository;
  @NonNull
  private final PartitionCreator partitionCreator;
  @NonNull
  private final Clock clock;

  @PostConstruct
  @Scheduled(cron = "@monthly")
  public void init() {
    createPartitions();
  }

  private void createPartitions() {
    OffsetDateTime now = now(clock);
    partitionCreator.createPartition(TABLE_NAME, now);
    partitionCreator.createPartition(TABLE_NAME, now.plusMonths(1));
  }

  public UUID create(@NonNull UUID batchId, @NonNull JsonNode payload) {
    MessageEntity messageEntity = MessageEntity.builder()
        .batchId(batchId)
        .messageId(messageIdGenerator.generate())
        .payload(payload)
        .build();
    return messageRepository.save(messageEntity).getMessageId();
  }
}
