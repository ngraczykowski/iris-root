package com.silenteight.scb.ingest.adapter.incomming.common.store.rawalert;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert;

import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RawAlertService {

  private final RawAlertRepository repository;

  public void store(String internalBatchId, List<Alert> alert) {
    log.info("Saving {} Raw Alerts with internalBatchId: {}", alert.size(), internalBatchId);
    repository.saveAll(RawAlertMapper.toRawAlertEntities(alert, internalBatchId));
  }
}
