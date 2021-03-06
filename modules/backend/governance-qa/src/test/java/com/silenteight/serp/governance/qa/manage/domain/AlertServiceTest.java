package com.silenteight.serp.governance.qa.manage.domain;

import com.silenteight.auditing.bs.AuditingLogger;
import com.silenteight.serp.governance.qa.manage.domain.dto.EraseAlertRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import javax.persistence.EntityNotFoundException;

import static com.silenteight.serp.governance.qa.AlertFixture.ALERT_NAME;
import static com.silenteight.serp.governance.qa.manage.domain.DecisionLevel.ANALYSIS;
import static com.silenteight.serp.governance.qa.manage.domain.DecisionState.FAILED;
import static java.lang.String.format;
import static java.time.OffsetDateTime.parse;
import static java.util.Optional.empty;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlertServiceTest {

  private static final long ALERT_ID = 1L;
  private static final OffsetDateTime NOW = parse("2021-09-14T08:09:25.481564Z");

  private InMemoryAlertRepository alertRepository;

  private InMemoryDecisionRepository decisionRepository;

  @Mock
  private AuditingLogger auditingLogger;

  private AlertService underTest;

  @BeforeEach
  void setUp() {
    alertRepository = new InMemoryAlertRepository();
    decisionRepository = new InMemoryDecisionRepository(alertRepository);
    underTest = new AlertService(alertRepository, auditingLogger);
  }

  @Test
  void eraseAlertShouldDeleteAlert() {
    //given
    Long alertId = saveAlert(ALERT_NAME).getId();
    Decision decision = new Decision();
    decision.setAlertId(alertId);
    decision.setLevel(ANALYSIS.getValue());
    decision.setState(FAILED);
    decisionRepository.save(decision);

    //when
    underTest.eraseAlert(EraseAlertRequest.of(alertId, "governance-app", NOW));
    //then
    assertThatThrownBy(() -> alertRepository.getById(alertId))
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessageContaining(format("Entity %d not found", alertId));
  }

  @Test
  void eraseAlertShouldIgnoreNotFoundAlerts() {
    //given
    AlertRepository alertRepository = mock(AlertRepository.class);
    underTest = new DomainConfiguration().alertService(alertRepository, auditingLogger);
    when(alertRepository.findById(ALERT_ID)).thenReturn(empty());
    //when
    underTest.eraseAlert(EraseAlertRequest.of(ALERT_ID, "governance-app", NOW));
    //then
    verify(alertRepository, never()).delete(any());
    verify(auditingLogger, never()).log(any());
  }

  private Alert saveAlert(String alertName) {
    Alert alert = new Alert();
    alert.setAlertName(alertName);
    return alertRepository.save(alert);
  }
}
