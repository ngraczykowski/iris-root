package com.silenteight.serp.governance.qa.manage.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.auditing.bs.AuditingLogger;
import com.silenteight.serp.governance.qa.manage.analysis.details.DecisionAlreadyExistsException;
import com.silenteight.serp.governance.qa.manage.domain.dto.CreateDecisionRequest;
import com.silenteight.serp.governance.qa.manage.domain.dto.EraseDecisionCommentRequest;
import com.silenteight.serp.governance.qa.manage.domain.dto.UpdateDecisionRequest;
import com.silenteight.serp.governance.qa.manage.domain.exception.AlertAlreadyProcessedException;
import com.silenteight.serp.governance.qa.manage.domain.exception.WrongAlertNameException;
import com.silenteight.serp.governance.qa.send.SendAlertMessageCommand;
import com.silenteight.serp.governance.qa.send.SendAlertMessageUseCase;

import java.time.OffsetDateTime;
import java.util.List;
import javax.transaction.Transactional;

import static com.silenteight.serp.governance.qa.manage.domain.DecisionState.VIEWING;
import static java.util.List.of;

@RequiredArgsConstructor
@Slf4j
public class DecisionService {

  @NonNull
  private final AlertRepository alertRepository;
  @NonNull
  private final DecisionRepository decisionRepository;
  @NonNull
  private final AuditingLogger auditingLogger;
  @NonNull
  private final SendAlertMessageUseCase sendAlertMessageUseCase;

  @Transactional
  public Alert addAlert(String alertName) {
    Alert alert = new Alert();
    alert.setAlertName(alertName);
    return alertRepository.save(alert);
  }

  public Decision updateDecision(UpdateDecisionRequest request) {
    request.preAudit(auditingLogger::log);

    Decision decision = getDecisionByAlertNameAndLevel(
        request.getAlertName(), request.getLevel());
    decision.setComment(request.getComment());
    decision.setState(request.getState());
    decision.setDecidedAt(request.getCreatedAt());
    decision.setDecidedBy(request.getCreatedBy());
    decision = decisionRepository.save(decision);

    sendAlertMessageUseCase.activate(SendAlertMessageCommand.of(of(request.toAlertDto())));

    request.postAudit(auditingLogger::log);
    return decision;
  }

  @Transactional
  public void eraseComments(EraseDecisionCommentRequest request) {
    long alertId = request.getAlertId();

    request.preAudit(auditingLogger::log);
    log.debug("Erasing decision comments for alert: {}", alertId);
    decisionRepository.eraseCommentByAlertId(alertId, request.getCreatedAt());
    request.postAudit(auditingLogger::log);
  }

  private Decision getDecisionByAlertNameAndLevel(
      String alertName, DecisionLevel decisionLevel) {

    return decisionRepository
        .findByAlertNameAndLevel(alertName, decisionLevel.getValue())
        .orElseThrow(() -> {
          throw new WrongAlertNameException(alertName);
        });
  }

  private void assertDecisionNotExistsOnLevel(String alertName, DecisionLevel level) {
    if (decisionRepository.findByAlertNameAndLevel(alertName, level.getValue())
        .isPresent())
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

    sendAlertMessageUseCase.activate(SendAlertMessageCommand.of(of(request.toAlertDto())));

    request.postAudit(auditingLogger::log);
  }

  private Alert getAlert(String alertName) {
    return alertRepository.findByAlertName(alertName)
        .orElseThrow(() -> new WrongAlertNameException(alertName));
  }

  public void view(String alertName, DecisionLevel level) {
    Decision decision = decisionRepository
        .findByAlertNameAndLevel(alertName, level.getValue())
        .orElseThrow(() -> new WrongAlertNameException(alertName));
    assertCanBeProcessed(decision, alertName);
    decision.setUpdatedAt(OffsetDateTime.now());
    decision.setState(VIEWING);
    decisionRepository.save(decision);
  }

  private static void assertCanBeProcessed(Decision decision, String alertName) {
    if (!decision.canBeProcessed())
      throw new AlertAlreadyProcessedException(alertName);
  }

  public void restartViewingDecisions(OffsetDateTime viewedBefore, Integer limit) {
    List<Decision> decisions = decisionRepository.findAllByStateAndUpdatedAtOlderThan(
        VIEWING.toString(), viewedBefore, limit);
    decisions.forEach(this::saveViewingState);
  }

  private void saveViewingState(Decision decision) {
    decision.resetViewingState();
    decisionRepository.save(decision);
  }
}
