package com.silenteight.serp.governance.qa.manage.domain;

import com.silenteight.serp.governance.qa.manage.analysis.details.dto.AlertAnalysisDetailsDto;
import com.silenteight.serp.governance.qa.manage.analysis.list.dto.AlertAnalysisDto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.List;

import static com.silenteight.serp.governance.qa.AlertFixture.ALERT_ID;
import static com.silenteight.serp.governance.qa.AlertFixture.ALERT_NAME;
import static com.silenteight.serp.governance.qa.AlertFixture.generateAlertName;
import static com.silenteight.serp.governance.qa.manage.domain.DecisionLevel.ANALYSIS;
import static com.silenteight.serp.governance.qa.manage.domain.DecisionState.FAILED;
import static com.silenteight.serp.governance.qa.manage.domain.DecisionState.NEW;
import static com.silenteight.serp.governance.qa.manage.domain.DecisionState.VIEWING;
import static java.time.OffsetDateTime.parse;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AlertAnalysisQueryTest {

  private static final OffsetDateTime ALERT_BEFORE_DATE = parse("2021-05-27T12:12:12.654894+02:00");
  private static final OffsetDateTime ALERT_AFTER_DATE = parse("2021-05-28T12:12:12.654894+02:00");
  private InMemoryAlertRepository alertRepository;
  private InMemoryDecisionRepository decisionRepository;
  AlertAnalysisQuery underTest;

  @BeforeEach
  void setUp() {
    alertRepository = new InMemoryAlertRepository();
    decisionRepository = new InMemoryDecisionRepository(alertRepository);
    underTest = new DomainConfiguration().alertAnalysisQuery(alertRepository, decisionRepository);
  }

  @Test
  void listShouldReturnAlertsWithNewAndFailedStateNewerThanDate() {
    //given
    Alert alertBefore = alertRepository.save(getMockedAlert(ALERT_BEFORE_DATE));
    decisionRepository.save(getDecision(alertBefore.getId(), NEW));

    Alert alertFailedAfter = alertRepository.save(getMockedAlert(ALERT_AFTER_DATE));
    decisionRepository.save(getDecision(alertFailedAfter.getId(), FAILED));

    Alert alertNewAfter = alertRepository.save(getMockedAlert(ALERT_AFTER_DATE));
    decisionRepository.save(getDecision(alertNewAfter.getId(), NEW));

    Alert alertViewingAfter = alertRepository.save(getMockedAlert(ALERT_AFTER_DATE));
    decisionRepository.save(getDecision(alertViewingAfter.getId(), VIEWING));

    Alert alertViewingAfterSecond = alertRepository.save(getMockedAlert(ALERT_AFTER_DATE));
    decisionRepository.save(getDecision(alertViewingAfterSecond.getId(), VIEWING));
    //when
    List<AlertAnalysisDto> alertDtos = underTest.list(of(NEW, FAILED), ALERT_BEFORE_DATE,5);
    //then
    alertDtos.forEach(dto -> {
      assertThat(dto.getAddedAt()).isAfter(ALERT_BEFORE_DATE.toInstant());
      assertThat(dto.getState()).isIn(NEW, FAILED);
    });
    assertThat(alertDtos.size()).isEqualTo(2);
  }

  private Alert getMockedAlert(OffsetDateTime createdAt) {
    Alert alert = mock(Alert.class);
    doCallRealMethod().when(alert).setId(any());
    doCallRealMethod().when(alert).getId();
    when(alert.getAlertName()).thenReturn(generateAlertName());
    when(alert.getCreatedAt()).thenReturn(createdAt);
    return alert;
  }

  private Decision getDecision(Long alertId, DecisionState state) {
    Decision decision = new Decision();
    decision.setAlertId(alertId);
    decision.setState(state);
    decision.setLevel(ANALYSIS.getValue());
    return decision;
  }

  @Test
  void countShouldReturnNumberOfAlertsWithGivenStateNewerThanDate() {
    //given
    Alert alertBefore = alertRepository.save(getMockedAlert(ALERT_BEFORE_DATE));
    decisionRepository.save(getDecision(alertBefore.getId(), NEW));

    Alert alertFailedAfter = alertRepository.save(getMockedAlert(ALERT_AFTER_DATE));
    decisionRepository.save(getDecision(alertFailedAfter.getId(), FAILED));

    Alert alertNewAfter = alertRepository.save(getMockedAlert(ALERT_AFTER_DATE));
    decisionRepository.save(getDecision(alertNewAfter.getId(), NEW));

    Alert alertViewingAfter = alertRepository.save(getMockedAlert(ALERT_AFTER_DATE));
    decisionRepository.save(getDecision(alertViewingAfter.getId(), VIEWING));
    //when
    //then
    assertThat(underTest.count(of(NEW, VIEWING))).isEqualTo(3);
  }

  @Test
  void detailsWillReturnAnalysisDetails() {
    Alert alert = new Alert();
    alert.setAlertName(ALERT_NAME);
    alert = alertRepository.save(alert);
    Decision analysisDecision = decisionRepository.save(getDecision(alert.getId(), FAILED));

    AlertAnalysisDetailsDto analysisDetailsDto = underTest.details(ALERT_ID);
    assertThat(analysisDetailsDto).isNotNull();
    assertThat(analysisDetailsDto.getAlertName()).isEqualTo(alert.getAlertName());
    assertThat(analysisDetailsDto.getState()).isEqualTo(analysisDecision.getState());
    assertThat(analysisDetailsDto.getDecisionComment()).isEqualTo(analysisDecision.getComment());
    assertThat(analysisDetailsDto.getDecisionBy()).isEqualTo(analysisDecision.getDecidedBy());
    assertThat(analysisDetailsDto.getDecisionAt()).isEqualTo(analysisDecision.getDecidedAt());
    assertThat(analysisDetailsDto.getAddedAt()).isEqualTo(alert.getCreatedAt());
  }
}