package com.silenteight.serp.governance.qa.retention.personalinformation;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.serp.governance.qa.manage.domain.AlertQuery;
import com.silenteight.serp.governance.qa.manage.domain.DecisionService;
import com.silenteight.serp.governance.qa.manage.domain.dto.EraseDecisionCommentRequest;

import java.util.List;

import static com.silenteight.serp.governance.qa.manage.domain.DecisionLevel.ANALYSIS;
import static java.time.OffsetDateTime.now;
import static org.apache.commons.collections4.ListUtils.partition;

@RequiredArgsConstructor
@Slf4j
public class EraseDecisionCommentUseCase {

  static final String PRINCIPAL_NAME = "governance-app";

  @NonNull
  private final AlertQuery alertQuery;
  @NonNull
  private final DecisionService decisionService;

  private final int batchSize;

  public void activate(List<String> alerts) {
    log.info("Looking up for {} alerts.", alerts.size());
    partition(alerts, batchSize)
        .stream()
        .map(alertQuery::findIdsForDiscriminators)
        .forEach(this::eraseComments);
  }

  private static EraseDecisionCommentRequest getEraseDecisionCommentRequest(long alertId) {
    return EraseDecisionCommentRequest.of(alertId, ANALYSIS, PRINCIPAL_NAME, now());
  }

  private void eraseComments(List<Long> alertIds) {
    alertIds.forEach(
        alertId -> decisionService.eraseComments(getEraseDecisionCommentRequest(alertId)));
  }
}
