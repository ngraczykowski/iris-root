package com.silenteight.hsbc.bridge.bulk;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.bulk.exception.BulkProcessingNotCompletedException;
import com.silenteight.hsbc.bridge.bulk.repository.BulkQueryRepository;
import com.silenteight.hsbc.bridge.rest.model.output.BulkSolvedAlertsResponse;

import java.util.UUID;

import static com.silenteight.hsbc.bridge.bulk.BulkStatus.COMPLETED;

@RequiredArgsConstructor
@Slf4j
public class GetBulkResultsUseCase {

  private final BulkQueryRepository queryRepository;

  public BulkSolvedAlertsResponse getResults(UUID id) {
    var bulk = queryRepository.findById(id);

    if (bulk.getStatus() != COMPLETED) {
      throw new BulkProcessingNotCompletedException("Bulk processing is not completed");
    }

    var result = queryRepository.findById(id);
    var response = new BulkSolvedAlertsResponse();
    response.setBulkId(result.getId());
    response.setBulkStatus(com.silenteight.hsbc.bridge.rest.model.output.BulkStatus.valueOf(
        result.getStatus().name()));
    //response.alerts(result.getSolvedAlerts());
    return response;
  }
}
