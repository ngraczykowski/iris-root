package com.silenteight.hsbc.bridge.bulk;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.bulk.exception.BulkProcessingNotCompletedException;
import com.silenteight.hsbc.bridge.bulk.rest.output.BulkAlertItem;
import com.silenteight.hsbc.bridge.bulk.rest.output.BulkStatusResponse;

import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static com.silenteight.hsbc.bridge.bulk.BulkStatus.COMPLETED;
import static com.silenteight.hsbc.bridge.bulk.BulkStatus.DELIVERED;

@RequiredArgsConstructor
public class AcknowledgeBulkDeliveryUseCase {

  private final BulkRepository bulkRepository;

  @Transactional
  public BulkStatusResponse apply(@NonNull String id) {
    var result = bulkRepository.findById(id);

    if (result.getStatus() != COMPLETED) {
      throw new BulkProcessingNotCompletedException(id);
    }

    result.setStatus(DELIVERED);
    var bulk = bulkRepository.save(result);

    var response = new BulkStatusResponse();
    response.setBulkId(id);
    response.setBulkStatus(
        com.silenteight.hsbc.bridge.bulk.rest.output.BulkStatus.fromValue(
            bulk.getStatus().name()));
    response.setRequestedAlerts(getRequestedAlerts(result.getItems()));

    return response;
  }

  private List<BulkAlertItem> getRequestedAlerts(Collection<BulkItem> bulkItems) {
    return bulkItems.stream().map(r -> {
      var bulkItem = new BulkAlertItem();
      bulkItem.setId(r.getAlertExternalId());
      bulkItem.setStatus(
          com.silenteight.hsbc.bridge.bulk.rest.output.BulkStatus.fromValue(
              r.getStatus().name()));
      return bulkItem;
    }).collect(Collectors.toList());
  }
}
