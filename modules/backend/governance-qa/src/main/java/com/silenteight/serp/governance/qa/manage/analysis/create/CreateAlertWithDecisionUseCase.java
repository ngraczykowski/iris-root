package com.silenteight.serp.governance.qa.manage.analysis.create;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.serp.governance.qa.manage.domain.DecisionService;
import com.silenteight.serp.governance.qa.manage.domain.dto.CreateDecisionRequest;

import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static java.lang.String.format;
import static java.util.Optional.empty;

@RequiredArgsConstructor
@Slf4j
public class CreateAlertWithDecisionUseCase {

  @NonNull
  private final DecisionService decisionService;

  public Optional<CreateDecisionRequest> activate(CreateDecisionRequest request) {
    try {
      log.info("CreateDecisionRequest request received, request={}", request);
      decisionService.addAlert(request.getAlertName());
      decisionService.createDecision(request);
      log.debug("CreateDecisionRequest request processed.");
      return Optional.of(request);
    } catch (DataIntegrityViolationException exception) {
      log.error(
          format("Processing of alert %s omitted because of data integrity violation.",
              request.getAlertName()),
          exception);
    }

    return empty();
  }
}
