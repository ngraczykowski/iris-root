package com.silenteight.hsbc.bridge.bulk;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.bulk.exception.BatchIdNotFoundException;
import com.silenteight.hsbc.bridge.bulk.exception.BatchProcessingNotCompletedException;
import com.silenteight.hsbc.bridge.bulk.rest.BatchStatus;
import com.silenteight.hsbc.bridge.bulk.rest.BatchAlertItem;
import com.silenteight.hsbc.bridge.bulk.rest.BatchStatusResponse;

import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class AcknowledgeBulkDeliveryUseCase {

  private final BulkRepository bulkRepository;

  @Transactional
  public BatchStatusResponse apply(@NonNull String id) {
    var result = bulkRepository.findById(id);

    if (result.isEmpty()) {
      throw new BatchIdNotFoundException(id);
    }

    var batch = result.get();
    if (batch.isNotCompleted()) {
      throw new BatchProcessingNotCompletedException(id);
    }

    batch.delivered();
    bulkRepository.save(batch);

    var response = new BatchStatusResponse();
    response.setBatchId(id);
    response.setBatchStatus(
        BatchStatus.fromValue(
            batch.getStatus().name()));
    response.setRequestedAlerts(getRequestedAlerts(batch.getAlerts()));

    return response;
  }

  private List<BatchAlertItem> getRequestedAlerts(Collection<BulkAlertEntity> alerts) {
    return alerts.stream().map(r -> {
      var alertItem = new BatchAlertItem();
      alertItem.setId(r.getExternalId());
      alertItem.setStatus(BatchStatus.fromValue(r.getStatus().name()));
      return alertItem;
    }).collect(Collectors.toList());
  }
}
