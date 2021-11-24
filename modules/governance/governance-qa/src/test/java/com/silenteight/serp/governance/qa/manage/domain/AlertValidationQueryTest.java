package com.silenteight.serp.governance.qa.manage.domain;

import com.silenteight.serp.governance.qa.manage.validation.details.dto.AlertValidationDetailsDto;
import com.silenteight.serp.governance.qa.manage.validation.list.dto.AlertValidationDto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.List;

import static com.silenteight.serp.governance.qa.AlertFixture.ALERT_NAME;
import static com.silenteight.serp.governance.qa.AlertFixture.generateAlertName;
import static com.silenteight.serp.governance.qa.manage.domain.DecisionLevel.VALIDATION;
import static com.silenteight.serp.governance.qa.manage.domain.DecisionState.FAILED;
import static com.silenteight.serp.governance.qa.manage.domain.DecisionState.NEW;
import static com.silenteight.serp.governance.qa.manage.domain.DecisionState.VIEWING;
import static java.time.OffsetDateTime.parse;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AlertValidationQueryTest {

  private static final OffsetDateTime ALERT_BEFORE_DATE = parse("2021-05-27T12:12:12.654894+02:00");
  private static final OffsetDateTime ALERT_AFTER_DATE = parse("2021-05-28T12:12:12.654894+02:00");
  private final InMemoryAlertRepository alertRepository = new InMemoryAlertRepository();
  private final InMemoryDecisionRepository decisionRepository = new InMemoryDecisionRepository(
      alertRepository);
  private AlertValidationQuery underTest;

  @BeforeEach
  void setUp() {
    underTest = new DomainConfiguration()
        .alertValidationQuery(alertRepository, decisionRepository);
  }

  @Test
  void listShouldReturnAlertsWithNewAndViewingStateNewerThanDate() {
    //given
    Alert alertBefore = alertRepository.save(getMockedAlert(ALERT_BEFORE_DATE));
    decisionRepository.save(getDecision(alertBefore.getId(), NEW));

    Alert alertFailedAfter = alertRepository.save(getMockedAlert(ALERT_AFTER_DATE));
    Decision decisionFailedAfter = decisionRepository.save(getDecision(alertFailedAfter.getId(),
        FAILED));

    Alert alertNewAfter = alertRepository.save(getMockedAlert(ALERT_AFTER_DATE));
    decisionRepository.save(getDecision(alertNewAfter.getId(), NEW));
    //when
    List<AlertValidationDto> alertDtos = underTest.list(List.of(FAILED), ALERT_BEFORE_DATE,2);
    //then
    assertThat(alertDtos.size()).isEqualTo(1);
    assertThat(alertDtos.get(0).getAlertName()).isEqualTo(alertFailedAfter.getAlertName());
    assertThat(alertDtos.get(0).getState()).isEqualTo(decisionFailedAfter.getState());
    assertThat(alertDtos.get(0).getAddedAt()).isAfter(ALERT_BEFORE_DATE.toInstant());
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
    decision.setLevel(VALIDATION.getValue());
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

    //when
    assertThat(underTest.count(List.of(NEW, VIEWING))).isEqualTo(2);
  }

  @Test
  void detailsWillReturnValidationDetails() {
    Alert alert = new Alert();
    alert.setAlertName(ALERT_NAME);
    alert = alertRepository.save(alert);
    decisionRepository.save(getDecision(alert.getId(), NEW));
    Decision validationDecision = decisionRepository.save(getDecision(alert.getId(), NEW));

    AlertValidationDetailsDto validationDetailsDto = underTest.details(ALERT_NAME);
    assertThat(validationDetailsDto).isNotNull();
    assertThat(validationDetailsDto.getAlertName()).isEqualTo(alert.getAlertName());
    assertThat(validationDetailsDto.getState()).isEqualTo(validationDecision.getState());
    assertThat(validationDetailsDto.getDecisionComment())
        .isEqualTo(validationDecision.getComment());
    assertThat(validationDetailsDto.getDecisionBy()).isEqualTo(validationDecision.getDecidedBy());
    assertThat(validationDetailsDto.getDecisionAt()).isEqualTo(validationDecision.getDecidedAt());
    assertThat(validationDetailsDto.getAddedAt()).isEqualTo(alert.getCreatedAt());
  }
}
