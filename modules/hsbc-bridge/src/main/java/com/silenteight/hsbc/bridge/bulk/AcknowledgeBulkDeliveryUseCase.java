package com.silenteight.hsbc.bridge.bulk;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.bulk.exception.BulkProcessingNotCompletedException;
import com.silenteight.hsbc.bridge.bulk.rest.BatchStatus;
import com.silenteight.hsbc.bridge.bulk.rest.BatchAlertItem;
import com.silenteight.hsbc.bridge.bulk.rest.BatchStatusResponse;

import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static com.silenteight.hsbc.bridge.bulk.BulkStatus.COMPLETED;

@RequiredArgsConstructor
public class AcknowledgeBulkDeliveryUseCase {

  private final BulkRepository bulkRepository;

  @Transactional
  public BatchStatusResponse apply(@NonNull String id) {
    var result = bulkRepository.findById(id);

    if (result.getStatus() != COMPLETED) {
      throw new BulkProcessingNotCompletedException(id);
    }

    result.delivered();
    var bulk = bulkRepository.save(result);

    var response = new BatchStatusResponse();
    response.setBatchId(id);
    response.setBatchStatus(
        BatchStatus.fromValue(
            bulk.getStatus().name()));
    response.setRequestedAlerts(getRequestedAlerts(result.getAlerts()));

    return response;
  }

  //FIXME do not use entity here, map statuses
  private List<BatchAlertItem> getRequestedAlerts(Collection<BulkAlertEntity> alerts) {
    return alerts.stream().map(r -> {
      var bulkItem = new BatchAlertItem();
      bulkItem.setId(r.getExternalId());
      bulkItem.setStatus(
          BatchStatus.fromValue(
              r.getStatus().name()));
      return bulkItem;
    }).collect(Collectors.toList());
  }
}
