package com.silenteight.serp.governance.qa.manage.domain;

import com.silenteight.auditing.bs.AuditingLogger;
import com.silenteight.serp.governance.qa.manage.analysis.details.DecisionAlreadyExistsException;
import com.silenteight.serp.governance.qa.manage.domain.dto.CreateDecisionRequest;
import com.silenteight.serp.governance.qa.manage.domain.dto.EraseDecisionCommentRequest;
import com.silenteight.serp.governance.qa.manage.domain.dto.UpdateDecisionRequest;
import com.silenteight.serp.governance.qa.manage.domain.exception.AlertAlreadyProcessedException;
import com.silenteight.serp.governance.qa.manage.domain.exception.WrongAlertNameException;
import com.silenteight.serp.governance.qa.send.SendAlertMessageCommand;
import com.silenteight.serp.governance.qa.send.SendAlertMessageUseCase;
import com.silenteight.serp.governance.qa.send.dto.AlertDto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;

import static com.silenteight.serp.governance.qa.AlertFixture.ALERT_NAME;
import static com.silenteight.serp.governance.qa.AlertFixture.generateAlertName;
import static com.silenteight.serp.governance.qa.DecisionFixture.*;
import static com.silenteight.serp.governance.qa.manage.domain.DecisionLevel.ANALYSIS;
import static com.silenteight.serp.governance.qa.manage.domain.DecisionLevel.VALIDATION;
import static com.silenteight.serp.governance.qa.manage.domain.DecisionState.FAILED;
import static com.silenteight.serp.governance.qa.manage.domain.DecisionState.NEW;
import static com.silenteight.serp.governance.qa.manage.domain.DecisionState.VIEWING;
import static java.lang.String.format;
import static java.time.OffsetDateTime.now;
import static java.time.OffsetDateTime.parse;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DecisionServiceTest {

  private static final OffsetDateTime CREATED_AT = parse("2020-05-21T01:01:01+01:00");

  private InMemoryAlertRepository alertRepository;

  private InMemoryDecisionRepository decisionRepository;

  private DecisionService underTest;

  @Mock
  private AuditingLogger auditingLogger;

  @Mock
  private SendAlertMessageUseCase sendAlertMessageUseCase;

  @Captor
  private ArgumentCaptor<SendAlertMessageCommand> messageCommandCaptor;

  @BeforeEach
  void setUp() {
    alertRepository = new InMemoryAlertRepository();
    decisionRepository = new InMemoryDecisionRepository(alertRepository);
    underTest = new DomainConfiguration().decisionService(alertRepository,
        decisionRepository, auditingLogger, sendAlertMessageUseCase);
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
    verify(auditingLogger, times(2)).log(any());
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
        .hasMessageContaining(
            format("Decision for alert alertName=%s on level=%d already exists.",
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
        .hasMessageContaining(format("Could not find alert with alertName=%s", ALERT_NAME));
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
  void viewDecisionWillChangeAnalysisDecisionsUpdatedAtAndState() {
    saveAlert(ALERT_NAME);
    underTest.createDecision(getCreateDecisionRequest(LEVEL_ANALYSIS));
    Decision createdAnalysis = getDecisionByAlertNameAndLevel(
        ALERT_NAME,
        LEVEL_ANALYSIS.getValue());
    underTest.createDecision(getCreateDecisionRequest(LEVEL_VALIDATION));
    Decision createdValidation = getDecisionByAlertNameAndLevel(
        ALERT_NAME,
        LEVEL_VALIDATION.getValue());
    assertThat(createdAnalysis.getState()).isEqualTo(NEW);
    assertThat(createdAnalysis.getUpdatedAt()).isNull();
    underTest.view(ALERT_NAME, LEVEL_ANALYSIS);
    Decision updated = getDecisionByAlertNameAndLevel(ALERT_NAME, LEVEL_ANALYSIS.getValue());
    assertThat(updated.getUpdatedAt()).isEqualToIgnoringSeconds(OffsetDateTime.now());
    assertThat(updated.getState()).isEqualTo(VIEWING);
    assertThat(createdValidation.getUpdatedAt()).isNull();
  }

  @Test
  void viewDecisionAfterUpdateWillThrowException() {
    saveAlert(ALERT_NAME);
    underTest.createDecision(getCreateDecisionRequest(LEVEL_ANALYSIS));
    underTest.updateDecision(getUpdateDecisionRequestForFailed());
    assertThatThrownBy(() -> underTest.view(ALERT_NAME, LEVEL_ANALYSIS))
        .isInstanceOf(AlertAlreadyProcessedException.class)
        .hasMessageContaining(
            format("Alert with with alertName=%s already processed", ALERT_NAME));
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

  @Test
  void restartViewingDecisionsShouldSetNewState() {
    //given
    OffsetDateTime viewedBefore = parse("2021-06-06T13:30:30+05:00");
    OffsetDateTime viewedAfter = parse("2021-06-07T13:30:30+05:00");
    Decision analysisViewingBefore = saveDecision(ANALYSIS.getValue(), VIEWING, viewedBefore,
        getGeneratedAlert().getId());
    Decision analysisViewingAfter = saveDecision(ANALYSIS.getValue(), VIEWING, viewedAfter,
        getGeneratedAlert().getId());
    Decision validationViewingBefore = saveDecision(VALIDATION.getValue(), VIEWING, viewedBefore,
        getGeneratedAlert().getId());
    Decision validationNewBefore = saveDecision(VALIDATION.getValue(), NEW, viewedBefore,
        getGeneratedAlert().getId());
    //when
    underTest.restartViewingDecisions(viewedAfter, 10);
    //then
    assertThat(decisionRepository.getById(analysisViewingBefore.getId()).getState())
        .isEqualTo(NEW);
    assertThat(decisionRepository.getById(analysisViewingBefore.getId()).getUpdatedAt())
        .isAfter(viewedAfter);
    assertThat(decisionRepository.getById(analysisViewingAfter.getId()).getState())
        .isEqualTo(VIEWING);
    assertThat(decisionRepository.getById(analysisViewingAfter.getId()).getUpdatedAt())
        .isEqualTo(viewedAfter);
    assertThat(decisionRepository.getById(validationViewingBefore.getId()).getState())
        .isEqualTo(NEW);
    assertThat(decisionRepository.getById(validationViewingBefore.getId()).getUpdatedAt())
        .isAfter(viewedAfter);
    assertThat(decisionRepository.getById(validationNewBefore.getId()).getState())
        .isEqualTo(NEW);
    assertThat(decisionRepository.getById(validationNewBefore.getId()).getUpdatedAt())
        .isEqualTo(viewedBefore);
  }

  private Alert getGeneratedAlert() {
    Alert alert = new Alert();
    alert.setAlertName(generateAlertName());
    return alertRepository.save(alert);
  }

  private Decision saveDecision(
      Integer level, DecisionState state, OffsetDateTime updatedAt, Long alertId) {

    Decision decision = new Decision();
    decision.setAlertId(alertId);
    decision.setLevel(level);
    decision.setState(state);
    decision.setUpdatedAt(updatedAt);
    return decisionRepository.save(decision);
  }

  @Test
  void createDecisionShouldSendAlertMessageCommandWithOneAlert() {
    //given
    Alert alert = saveAlert(ALERT_NAME);
    //when
    underTest.createDecision(getCreateDecisionRequest(LEVEL_ANALYSIS));
    //then
    verify(sendAlertMessageUseCase, times(1))
        .activate(messageCommandCaptor.capture());
    assertThat(messageCommandCaptor.getValue().getAlertDtos().size()).isEqualTo(1);
    assertThat(messageCommandCaptor.getValue().getAlertDtos().size()).isEqualTo(1);
    AlertDto alertDto = messageCommandCaptor.getValue().getAlertDtos().get(0);
    assertThat(alertDto.getAlertName()).isEqualTo(alert.getAlertName());
    assertThat(alertDto.getLevel()).isEqualTo(ANALYSIS);
    assertThat(alertDto.getState()).isEqualTo(NEW);
    assertThat(alertDto.getComment()).isNull();
  }

  @Test
  void updateDecisionShouldSendSendAlertMessageCommandWithOneAlert() {
    //given
    Alert alert = saveAlert(ALERT_NAME);
    saveDecision(LEVEL_ANALYSIS.getValue(), NEW, now(), alert.getId());
    //when
    underTest.updateDecision(getUpdateDecisionRequestForFailed());
    //then
    verify(sendAlertMessageUseCase, times(1))
        .activate(messageCommandCaptor.capture());
    assertThat(messageCommandCaptor.getValue().getAlertDtos().size()).isEqualTo(1);
    assertThat(messageCommandCaptor.getValue().getAlertDtos().size()).isEqualTo(1);
    AlertDto alertDto = messageCommandCaptor.getValue().getAlertDtos().get(0);
    assertThat(alertDto.getAlertName()).isEqualTo(alert.getAlertName());
    assertThat(alertDto.getLevel()).isEqualTo(ANALYSIS);
    assertThat(alertDto.getState()).isEqualTo(FAILED);
    assertThat(alertDto.getComment()).isEqualTo(COMMENT_FAILED);
  }

  @Test
  void eraseCommentShouldSetNullForDecisionComment() {
    //given
    Alert alert = saveAlert(ALERT_NAME);
    Decision decision = saveDecision(LEVEL_ANALYSIS.getValue(), NEW, CREATED_AT, alert.getId());
    //when
    underTest.eraseComments(EraseDecisionCommentRequest.of(alert.getId(), LEVEL_ANALYSIS,
        "governance-app", CREATED_AT));
    //then
    Decision updated = decisionRepository.getById(decision.getId());
    assertThat(updated.getComment()).isNull();
    assertThat(updated.getState()).isEqualTo(decision.getState());
    assertThat(updated.getLevel()).isEqualTo(decision.getLevel());
  }
}
