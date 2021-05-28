package com.silenteight.hsbc.bridge.bulk;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.analysis.AnalysisFacade;
import com.silenteight.hsbc.bridge.bulk.exception.BatchIdNotFoundException;
import com.silenteight.hsbc.bridge.bulk.rest.BatchAlertItem;
import com.silenteight.hsbc.bridge.bulk.rest.BatchStatus;
import com.silenteight.hsbc.bridge.bulk.rest.BatchStatusResponse;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static com.silenteight.hsbc.bridge.bulk.BulkStatus.PRE_PROCESSED;
import static com.silenteight.hsbc.bridge.bulk.BulkStatus.PROCESSING;
import static com.silenteight.hsbc.bridge.bulk.BulkStatus.STORED;
import static java.util.List.of;

@RequiredArgsConstructor
public class GetBulkStatusUseCase {

  private final AnalysisFacade analysisFacade;
  private final BulkRepository bulkRepository;

  public BatchStatusResponse getStatus(String id) {
    var result = bulkRepository.findById(id);

    if (result.isEmpty()) {
      throw new BatchIdNotFoundException(id);
    }

    var batch = result.get();
    var response = new BatchStatusResponse();
    response.setBatchId(id);
    response.setBatchStatus(BatchStatus.fromValue(batch.getStatus().name()));
    response.setRequestedAlerts(getRequestedAlerts(batch.getAlerts()));

    if (batch.hasAnalysisId()) {
      var analysis = analysisFacade.getById(batch.getAnalysisId());
      response.setPolicyName(analysis.getPolicy());
    }

    return response;
  }

  public boolean isProcessing() {
    return bulkRepository.existsByStatusIn(of(STORED, PRE_PROCESSED, PROCESSING));
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
