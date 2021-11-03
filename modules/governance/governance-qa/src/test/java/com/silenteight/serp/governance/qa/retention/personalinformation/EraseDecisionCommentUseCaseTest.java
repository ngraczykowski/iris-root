package com.silenteight.serp.governance.qa.retention.personalinformation;

import com.silenteight.serp.governance.qa.manage.domain.AlertQuery;
import com.silenteight.serp.governance.qa.manage.domain.DecisionService;
import com.silenteight.serp.governance.qa.manage.domain.dto.EraseDecisionCommentRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.silenteight.serp.governance.qa.manage.domain.DecisionLevel.ANALYSIS;
import static com.silenteight.serp.governance.qa.retention.personalinformation.EraseDecisionCommentUseCase.PRINCIPAL_NAME;
import static java.util.Collections.emptyList;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EraseDecisionCommentUseCaseTest {

  private static final long ALERT_ID_3 = 3L;
  private static final String ALERT_DISCRIMINATOR_1 = "54673323-7df0-484a-92d2-4a9c4ec6c55a";
  private static final String ALERT_DISCRIMINATOR_2 = "d6a0db14-03cd-4026-9967-9787106dc9be";
  private static final String ALERT_DISCRIMINATOR_3 = "dd8103bc-787c-4e59-8065-cf6368659214";
  private static final String ALERT_DISCRIMINATOR_EMPTY = "";

  private EraseDecisionCommentUseCase underTest;

  @Mock
  AlertQuery alertQuery;
  @Mock
  private DecisionService decisionService;

  private final int batchSize = 2;

  @BeforeEach
  void setUp() {
    underTest = new EraseDecisionCommentUseCase(alertQuery, decisionService, batchSize);
  }

  @Test
  void activateShouldEraseOneDecisionComment() {
    //given
    List<String> alerts = of(ALERT_DISCRIMINATOR_1, ALERT_DISCRIMINATOR_2, ALERT_DISCRIMINATOR_3,
        ALERT_DISCRIMINATOR_EMPTY);

    ArgumentCaptor<EraseDecisionCommentRequest> eraseDecisionCommentRequestCaptor =
        ArgumentCaptor.forClass(EraseDecisionCommentRequest.class);
    when(alertQuery.findIdsForDiscriminators(of(ALERT_DISCRIMINATOR_1, ALERT_DISCRIMINATOR_2)))
        .thenReturn(emptyList());
    when(alertQuery.findIdsForDiscriminators(of(ALERT_DISCRIMINATOR_3, ALERT_DISCRIMINATOR_EMPTY)))
        .thenReturn(of(ALERT_ID_3));

    //when
    underTest.activate(alerts);
    //then
    verify(decisionService,  times(1))
        .eraseComments(eraseDecisionCommentRequestCaptor.capture());
    EraseDecisionCommentRequest requestCaptured = eraseDecisionCommentRequestCaptor.getValue();

    assertThat(requestCaptured.getAlertId()).isEqualTo(ALERT_ID_3);
    assertThat(requestCaptured.getCreatedBy()).isEqualTo(PRINCIPAL_NAME);
    assertThat(requestCaptured.getCorrelationId()).isNotNull();
    assertThat(requestCaptured.getLevel()).isEqualTo(ANALYSIS);
    assertThat(requestCaptured.getCreatedAt()).isNotNull();
  }

  @Test
  void activateShouldNotEraseDecisionCommentWhenNoAlertsFound() {
    //given
    List<String> alerts = of(ALERT_DISCRIMINATOR_1, ALERT_DISCRIMINATOR_2);
    when(alertQuery.findIdsForDiscriminators(alerts)).thenReturn(emptyList());
    //when
    underTest.activate(alerts);
    //then
    verify(decisionService,  never()).eraseComments(any());
  }
}
