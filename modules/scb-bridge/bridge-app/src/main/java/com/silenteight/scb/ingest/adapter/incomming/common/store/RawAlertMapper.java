package com.silenteight.scb.ingest.adapter.incomming.common.store;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert;
import com.silenteight.scb.ingest.adapter.incomming.common.store.RawAlert.AlertType;
import com.silenteight.scb.ingest.adapter.incomming.common.util.SerializationUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.vavr.control.Try;

import java.util.List;

@Slf4j
@UtilityClass
class RawAlertMapper {

  List<RawAlert> toRawAlertEntities(List<Alert> alerts, String internalBatchId) {
    return alerts.stream()
        .map(alert -> toRawAlertEntity(alert, internalBatchId))
        .toList();
  }

  private RawAlert toRawAlertEntity(Alert alert, String internalBatchId) {
    var systemId = alert.details().getSystemId();
    var batchId = alert.details().getBatchId();
    var alertType = getAlertType(alert);
    return new RawAlert(systemId, batchId, internalBatchId, alertType, toPayload(alert));
  }

  private AlertType getAlertType(Alert alert) {
    return alert.isLearnFlag() ? AlertType.LEARNING : AlertType.SOLVING;
  }

  private byte[] toPayload(Alert alert) {
    return Try.of(() -> serializeAlert(alert))
        .onFailure(e -> log.error("Failed to serialize alert with systemId: {}.",
            alert.details().getSystemId(), e))
        .get();
  }

  private byte[] serializeAlert(Alert alert) throws JsonProcessingException {
    return SerializationUtils.serialize(alert);
  }
}
