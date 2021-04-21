package com.silenteight.hsbc.bridge.bulk;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.analysis.AnalysisFacade;
import com.silenteight.hsbc.bridge.bulk.rest.output.BulkAlertItem;
import com.silenteight.hsbc.bridge.bulk.rest.output.BulkProcessingStatusResponse;
import com.silenteight.hsbc.bridge.bulk.rest.output.BulkStatus;
import com.silenteight.hsbc.bridge.bulk.rest.output.BulkStatusResponse;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static com.silenteight.hsbc.bridge.bulk.BulkStatus.PROCESSING;
import static com.silenteight.hsbc.bridge.bulk.BulkStatus.STORED;

@RequiredArgsConstructor
public class GetBulkStatusUseCase {

  private final AnalysisFacade analysisFacade;
  private final BulkRepository bulkRepository;

  public BulkStatusResponse getStatus(String id) {
    var bulk = bulkRepository.findById(id);

    var response = new BulkStatusResponse();
    response.setBulkId(id);
    response.setBulkStatus(BulkStatus.fromValue(bulk.getStatus().name()));
    response.setRequestedAlerts(getRequestedAlerts(bulk.getItems()));

    if (bulk.hasAnalysisId()) {
      var analysis = analysisFacade.getById(bulk.getAnalysisId());
      response.setPolicyName(analysis.getPolicy());
    }

    return response;
  }

  public BulkProcessingStatusResponse isProcessing() {
    var result = bulkRepository.existsByStatusIn(List.of(STORED, PROCESSING));

    var response = new BulkProcessingStatusResponse();
    response.setIsAdjudicationEngineProcessing(result);

    return response;
  }

  private List<BulkAlertItem> getRequestedAlerts(Collection<BulkItem> bulkItems) {
    return bulkItems.stream().map(r -> {
      var bulkItem = new BulkAlertItem();
      bulkItem.setId(r.getAlertExternalId());
      bulkItem.setStatus(BulkStatus.fromValue(r.getStatus().name()));
      return bulkItem;
    }).collect(Collectors.toList());
  }
}
