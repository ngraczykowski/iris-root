package com.silenteight.connector.ftcc.request.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.connector.ftcc.common.database.partition.PartitionCreator;

import org.springframework.scheduling.annotation.Scheduled;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.UUID;
import javax.annotation.PostConstruct;

import static java.time.OffsetDateTime.now;

@Slf4j
@RequiredArgsConstructor
public class RequestService {

  private static final String TABLE_NAME = "ftcc_request";

  @NonNull
  private final RequestRepository requestRepository;
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

  public void create(@NonNull UUID batchId) {
    RequestEntity requestEntity = RequestEntity.builder()
        .batchId(batchId)
        .build();
    requestRepository.save(requestEntity);
  }
}
