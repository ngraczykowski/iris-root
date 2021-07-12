package com.silenteight.hsbc.bridge.bulk;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import com.silenteight.hsbc.bridge.alert.AlertStatus;
import com.silenteight.hsbc.bridge.bulk.rest.BatchAlertItem;
import com.silenteight.hsbc.bridge.bulk.rest.BatchAlertItemStatus;
import com.silenteight.hsbc.bridge.bulk.rest.BatchStatus;
import com.silenteight.hsbc.bridge.bulk.rest.BatchStatusResponse;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class BatchResponseCreator {

  static BatchStatusResponse create(@NonNull Bulk batch) {
    var response = new BatchStatusResponse();
    response.setBatchId(batch.getId());
    response.setBatchStatus(mapBatchStatus(batch.getStatus()));
    response.setRequestedAlerts(getRequestedAlerts(batch.getAlerts()));

    if (batch.hasAnalysis()) {
      var analysis = batch.getAnalysis();
      response.setPolicyName(analysis.getPolicy());
    }

    return response;
  }

  private static List<BatchAlertItem> getRequestedAlerts(Collection<BulkAlertEntity> alerts) {
    return alerts.stream().map(r -> {
      var alertItem = new BatchAlertItem();
      alertItem.setId(r.getExternalId());
      alertItem.setStatus(mapBatchAlertStatus(r.getStatus()));
      alertItem.setErrorMessage(r.getErrorMessage());
      return alertItem;
    }).collect(Collectors.toList());
  }

  private static BatchStatus mapBatchStatus(@NonNull BulkStatus status) {
    if (status.isInternal()) {
      return BatchStatus.PROCESSING;
    }

    return BatchStatus.fromValue(status.name());
  }

  private static BatchAlertItemStatus mapBatchAlertStatus(@NonNull AlertStatus status) {
    if (status.isInternal()) {
      return BatchAlertItemStatus.PROCESSING;
    }

    return BatchAlertItemStatus.fromValue(status.name());
  }
}
