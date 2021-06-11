package com.silenteight.serp.governance.qa.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.auditing.bs.AuditingLogger;
import com.silenteight.serp.governance.qa.analysis.details.DecisionAlreadyExistsException;
import com.silenteight.serp.governance.qa.domain.dto.CreateDecisionRequest;
import com.silenteight.serp.governance.qa.domain.dto.UpdateDecisionRequest;
import com.silenteight.serp.governance.qa.domain.exception.AlertAlreadyProcessedException;
import com.silenteight.serp.governance.qa.domain.exception.WrongAlertNameException;

import java.time.OffsetDateTime;
import javax.transaction.Transactional;

import static com.silenteight.serp.governance.qa.domain.DecisionState.NEW;

@RequiredArgsConstructor
public class DecisionService {

  @NonNull
  private final AlertRepository alertRepository;
  @NonNull
  private final DecisionRepository decisionRepository;
  @NonNull
  private final AuditingLogger auditingLogger;

  public Decision updateDecision(UpdateDecisionRequest request) {
    request.preAudit(auditingLogger::log);

    Decision decision = getDecisionByAlertNameAndLevel(request.getAlertName(), request.getLevel());
    decision.setComment(request.getComment());
    decision.setState(request.getState());
    decision.setDecidedAt(request.getCreatedAt());
    decision.setDecidedBy(request.getCreatedBy());
    decision = decisionRepository.save(decision);

    request.postAudit(auditingLogger::log);
    return decision;
  }

  private Decision getDecisionByAlertNameAndLevel(String alertName, DecisionLevel decisionLevel) {
    return decisionRepository
        .findByAlertNameAndLevel(alertName, decisionLevel.getValue())
        .orElseThrow(() -> {
          throw new WrongAlertNameException(alertName);
        });
  }

  private void assertDecisionNotExistsOnLevel(String alertName, DecisionLevel level) {
    if (decisionRepository.findByAlertNameAndLevel(alertName, level.getValue()).isPresent())
      throw new DecisionAlreadyExistsException(alertName, level);
  }

  @Transactional
  public void createDecision(CreateDecisionRequest request) {
    request.preAudit(auditingLogger::log);
    assertDecisionNotExistsOnLevel(request.getAlertName(), request.getLevel());
    Alert alert = getAlert(request.getAlertName());

    Decision decision = new Decision();
    decision.setAlertId(alert.getId());
    decision.setState(request.getState());
    decision.setLevel(request.getLevel().getValue());
    decisionRepository.save(decision);
    request.postAudit(auditingLogger::log);
  }

  private Alert getAlert(String alertName) {
    Alert alert = alertRepository.findByAlertName(alertName);
    if (alert == null)
      throw new WrongAlertNameException(alertName);
    return alert;
  }

  public void view(String alertName, DecisionLevel level) {
    Decision decision = decisionRepository.findByAlertNameAndLevel(alertName, level.getValue())
        .orElseThrow(() -> new WrongAlertNameException(alertName));
    assertCanBeProcessed(decision, alertName);
    decision.setUpdatedAt(OffsetDateTime.now());
    decisionRepository.save(decision);
  }

  private static void assertCanBeProcessed(Decision decision, String alertName) {
    if (!decision.canBeProcessed())
      throw new AlertAlreadyProcessedException(alertName);
  }

  public void restartDecisionState(Decision decision) {
    decision.setState(NEW);
    decision.setUpdatedAt(OffsetDateTime.now());
    decisionRepository.save(decision);
  }
}
