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
    repository.saveAll(RawAlertMapper.toRawAlertEntities(alert, internalBatchId));
    log.info(
        "Raw alerts have been stored with internalBatchId: {} and alertSize: {}",
        internalBatchId, alert.size());
  }
}
