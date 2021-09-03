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
import com.silenteight.serp.governance.qa.manage.domain.exception.WrongDiscriminatorException;
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
  public Alert addAlert(String discriminator) {
    Alert alert = new Alert();
    alert.setDiscriminator(discriminator);
    return alertRepository.save(alert);
  }

  public Decision updateDecision(UpdateDecisionRequest request) {
    request.preAudit(auditingLogger::log);

    Decision decision = getDecisionByDiscriminatorAndLevel(
        request.getDiscriminator(), request.getLevel());
    decision.setComment(request.getComment());
    decision.setState(request.getState());
    decision.setDecidedAt(request.getCreatedAt());
    decision.setDecidedBy(request.getCreatedBy());
    decision = decisionRepository.save(decision);

    sendAlertMessageUseCase.activate(SendAlertMessageCommand.of(of(request.toAlertDto())));

    request.postAudit(auditingLogger::log);
    return decision;
  }

  public Decision eraseComment(EraseDecisionCommentRequest request) {
    log.debug("Erasing decision comment: {}", request);

    request.preAudit(auditingLogger::log);

    Decision decision = getDecisionByDiscriminatorAndLevel(
        request.getDiscriminator(), request.getLevel());
    decision.eraseComment();
    decision = decisionRepository.save(decision);

    request.postAudit(auditingLogger::log);

    return decision;
  }

  private Decision getDecisionByDiscriminatorAndLevel(
      String discriminator, DecisionLevel decisionLevel) {

    return decisionRepository
        .findByDiscriminatorAndLevel(discriminator, decisionLevel.getValue())
        .orElseThrow(() -> {
          throw new WrongDiscriminatorException(discriminator);
        });
  }

  private void assertDecisionNotExistsOnLevel(String discriminator, DecisionLevel level) {
    if (decisionRepository.findByDiscriminatorAndLevel(discriminator, level.getValue())
        .isPresent())
      throw new DecisionAlreadyExistsException(discriminator, level);
  }

  @Transactional
  public void createDecision(CreateDecisionRequest request) {
    request.preAudit(auditingLogger::log);
    assertDecisionNotExistsOnLevel(request.getDiscriminator(), request.getLevel());
    Alert alert = getAlert(request.getDiscriminator());

    Decision decision = new Decision();
    decision.setAlertId(alert.getId());
    decision.setState(request.getState());
    decision.setLevel(request.getLevel().getValue());
    decisionRepository.save(decision);

    sendAlertMessageUseCase.activate(SendAlertMessageCommand.of(of(request.toAlertDto())));

    request.postAudit(auditingLogger::log);
  }

  private Alert getAlert(String discriminator) {
    Alert alert = alertRepository.findByDiscriminator(discriminator);
    if (alert == null)
      throw new WrongDiscriminatorException(discriminator);
    return alert;
  }

  public void view(String discriminator, DecisionLevel level) {
    Decision decision = decisionRepository
        .findByDiscriminatorAndLevel(discriminator, level.getValue())
        .orElseThrow(() -> new WrongDiscriminatorException(discriminator));
    assertCanBeProcessed(decision, discriminator);
    decision.setUpdatedAt(OffsetDateTime.now());
    decisionRepository.save(decision);
  }

  private static void assertCanBeProcessed(Decision decision, String discriminator) {
    if (!decision.canBeProcessed())
      throw new AlertAlreadyProcessedException(discriminator);
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
