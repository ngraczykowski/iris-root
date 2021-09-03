package com.silenteight.serp.governance.qa.retention.erase;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.serp.governance.qa.manage.domain.DecisionService;
import com.silenteight.serp.governance.qa.manage.domain.dto.EraseDecisionCommentRequest;

import java.util.List;

import static com.silenteight.serp.governance.qa.manage.domain.DecisionLevel.ANALYSIS;
import static java.time.OffsetDateTime.now;

@RequiredArgsConstructor
@Slf4j
public class EraseDecisionCommentUseCase {

  static final String PRINCIPAL_NAME = "governance-app";

  @NonNull
  private final DecisionService decisionService;

  public void activate(List<String> alerts) {
    log.info("Erasing decision comments for {} alerts.", alerts.size());
    alerts.forEach(
        discriminator -> decisionService.eraseComment(
            getEraseDecisionCommentRequest(discriminator)));
  }

  private static EraseDecisionCommentRequest getEraseDecisionCommentRequest(String discriminator) {
    return EraseDecisionCommentRequest.of(discriminator, ANALYSIS, PRINCIPAL_NAME, now());
  }
}
