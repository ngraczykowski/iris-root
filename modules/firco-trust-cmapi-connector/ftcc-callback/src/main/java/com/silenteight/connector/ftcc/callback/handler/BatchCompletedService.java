package com.silenteight.connector.ftcc.callback.handler;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.connector.ftcc.common.database.partition.PartitionCreator;
import com.silenteight.connector.ftcc.common.resource.BatchResource;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.OffsetDateTime;
import javax.annotation.PostConstruct;

import static java.time.OffsetDateTime.now;

@RequiredArgsConstructor
public class BatchCompletedService {

  private static final String TABLE_NAME = "ftcc_batch_completed";

  @NonNull
  private final BatchCompletedRepository batchCompletedRepository;
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

  @Async
  @Transactional
  public void save(String batchId, String analysisId) {
    batchCompletedRepository.save(BatchCompletedEntity.builder()
        .batchId(BatchResource.fromResourceName(batchId))
        .analysisId(analysisId)
        .build());
  }
}
