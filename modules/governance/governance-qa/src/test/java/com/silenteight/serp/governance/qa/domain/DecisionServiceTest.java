package com.silenteight.serp.governance.qa.domain;

import com.silenteight.auditing.bs.AuditingLogger;
import com.silenteight.serp.governance.qa.analysis.details.DecisionAlreadyExistsException;
import com.silenteight.serp.governance.qa.domain.dto.CreateDecisionRequest;
import com.silenteight.serp.governance.qa.domain.dto.UpdateDecisionRequest;
import com.silenteight.serp.governance.qa.domain.exception.AlertAlreadyProcessedException;
import com.silenteight.serp.governance.qa.domain.exception.WrongAlertNameException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;

import static com.silenteight.serp.governance.qa.AlertFixture.ALERT_NAME;
import static com.silenteight.serp.governance.qa.DecisionFixture.*;
import static java.lang.String.format;
import static java.time.OffsetDateTime.parse;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DecisionServiceTest {

  private static final OffsetDateTime CREATED_AT = parse("2020-05-21T01:01:01+01:00");
  private final InMemoryAlertRepository alertRepository = new InMemoryAlertRepository();
  private final InMemoryDecisionRepository decisionRepository = new InMemoryDecisionRepository(
      alertRepository);
  @Mock
  private AuditingLogger auditingLogger;
  DecisionService underTest;

  @BeforeEach
  void setUp() {
    underTest = new DomainConfiguration().decisionService(alertRepository,
        decisionRepository, auditingLogger);
  }

  @Test
  void createDecisionWillSaveDecisionWithLevel() {
    Alert alert = saveAlert(ALERT_NAME);
    underTest.createDecision(getCreateDecisionRequest(LEVEL_ANALYSIS));
    Decision decision = getDecisionByAlertNameAndLevel(ALERT_NAME,LEVEL_ANALYSIS.getValue());
    assertThat(decision.getAlertId()).isEqualTo(alert.getId());
    assertThat(decision.getState()).isEqualTo(STATE_NEW);
    assertThat(decision.getLevel()).isEqualTo(LEVEL_ANALYSIS.getValue());
    assertThat(decision.getDecidedAt()).isNull();
    assertThat(decision.getComment()).isNull();
    assertThat(decision.getDecidedBy()).isNull();
    Mockito.verify(auditingLogger, times(2)).log(any());
  }

  @Test
  void createDecisionWillThrownExceptionIfOtherOnSameLevelExists() {
    saveAlert(ALERT_NAME);
    underTest.createDecision(getCreateDecisionRequest(LEVEL_ANALYSIS));
    Decision decision = getDecisionByAlertNameAndLevel(ALERT_NAME,LEVEL_ANALYSIS.getValue());
    assertThat(decision.getId()).isNotNull();

    CreateDecisionRequest request = getCreateDecisionRequest(LEVEL_ANALYSIS);
    assertThatThrownBy(() -> underTest.createDecision(request))
        .isInstanceOf(DecisionAlreadyExistsException.class)
        .hasMessageContaining(format("Decision for alert name=%s on level=%d already exists.",
            ALERT_NAME, LEVEL_ANALYSIS.getValue()));
  }

  private Decision getDecisionByAlertNameAndLevel(String alertName, Integer level) {
    return decisionRepository
        .findByAlertNameAndLevel(alertName, level)
        .orElseThrow(() -> new WrongAlertNameException(alertName));
  }

  @Test
  void createDecisionWillThrownExceptionIfAlertNotFound() {
    CreateDecisionRequest request = getCreateDecisionRequest(LEVEL_ANALYSIS);
    assertThatThrownBy(() -> underTest.createDecision(request))
        .isInstanceOf(WrongAlertNameException.class)
        .hasMessageContaining(format("Could not find alert with name=%s", ALERT_NAME));
  }

  private CreateDecisionRequest getCreateDecisionRequest(DecisionLevel level) {
    return CreateDecisionRequest.of(
        ALERT_NAME,
        STATE_NEW,
        level,
        DECIDED_BY,
        CREATED_AT);
  }

  @Test
  void updateDecisionWillChangeDecisionState() {
    saveAlert(ALERT_NAME);
    underTest.createDecision(getCreateDecisionRequest(LEVEL_ANALYSIS));
    UpdateDecisionRequest updateDecisionRequest = getUpdateDecisionRequestForFailed();
    Decision decision = underTest.updateDecision(updateDecisionRequest);
    Decision updated = decisionRepository.findById(decision.getId()).orElse(null);
    assertThat(updated).isNotNull();
    assertThat(updated.getState()).isEqualTo(STATE_FAILED);
    assertThat(updated.getComment()).isEqualTo(COMMENT_FAILED);
    assertThat(updated.getDecidedBy()).isEqualTo("username-failed");
    assertThat(updated.getDecidedAt()).isEqualTo(updateDecisionRequest.getCreatedAt());
    assertThat(updated.getDecidedAt()).isNotEqualTo(DECIDED_AT);
    assertThat(updated.getDecidedBy()).isNotEqualTo(DECIDED_BY);
    verify(auditingLogger, times(4)).log(any());
  }

  private Alert saveAlert(String alertName) {
    Alert alert = new Alert();
    alert.setAlertName(alertName);
    return alertRepository.save(alert);
  }

  @Test
  void viewDecisionWillChangeAnalysisDecisionsUpdatedAt() {
    saveAlert(ALERT_NAME);
    underTest.createDecision(getCreateDecisionRequest(LEVEL_ANALYSIS));
    Decision createdAnalysis = getDecisionByAlertNameAndLevel(ALERT_NAME,
        LEVEL_ANALYSIS.getValue());
    underTest.createDecision(getCreateDecisionRequest(LEVEL_VALIDATION));
    Decision createdValidation = getDecisionByAlertNameAndLevel(ALERT_NAME,
        LEVEL_VALIDATION.getValue());
    assertThat(createdAnalysis.getUpdatedAt()).isNull();
    underTest.view(ALERT_NAME, LEVEL_ANALYSIS);
    Decision updated = getDecisionByAlertNameAndLevel(ALERT_NAME, LEVEL_ANALYSIS.getValue());
    assertThat(updated.getUpdatedAt()).isEqualToIgnoringSeconds(OffsetDateTime.now());
    assertThat(createdValidation.getUpdatedAt()).isNull();
  }

  @Test
  void viewDecisionAfterUpdateWillThrowException() {
    saveAlert(ALERT_NAME);
    underTest.createDecision(getCreateDecisionRequest(LEVEL_ANALYSIS));
    underTest.updateDecision(getUpdateDecisionRequestForFailed());
    assertThatThrownBy(() -> underTest.view(ALERT_NAME, LEVEL_ANALYSIS))
        .isInstanceOf(AlertAlreadyProcessedException.class)
        .hasMessageContaining(format("Alert with with name=%s already processed", ALERT_NAME));
  }

  private UpdateDecisionRequest getUpdateDecisionRequestForFailed() {
    return UpdateDecisionRequest.of(
        ALERT_NAME,
        STATE_FAILED,
        LEVEL_ANALYSIS,
        COMMENT_FAILED,
        "username-failed",
        OffsetDateTime.now());
  }
}
