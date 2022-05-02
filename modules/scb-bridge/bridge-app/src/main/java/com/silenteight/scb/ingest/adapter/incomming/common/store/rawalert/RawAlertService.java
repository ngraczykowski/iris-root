package com.silenteight.scb.ingest.adapter.incomming.common.store.rawalert;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import javax.annotation.PostConstruct;

@Slf4j
@Service
@RequiredArgsConstructor
public class RawAlertService {

  private final RawAlertRepository repository;

  @PostConstruct
  @Scheduled(cron = "@monthly")
  public void init() {
    try {
      createPartitions();
    } catch (Exception e) {
      throw new IllegalStateException("Exception while creating partition for raw_alerts", e);
    }
  }

  public void store(String internalBatchId, List<Alert> alert) {
    log.info("Saving {} Raw Alerts with internalBatchId: {}", alert.size(), internalBatchId);
    repository.saveAll(RawAlertMapper.toRawAlertEntities(alert, internalBatchId));
  }

  public void createPartitions() {
    var currentDateTime = OffsetDateTime.now();
    repository.createPartition(currentDateTime);
    repository.createPartition(currentDateTime.plusMonths(1));
  }
}
