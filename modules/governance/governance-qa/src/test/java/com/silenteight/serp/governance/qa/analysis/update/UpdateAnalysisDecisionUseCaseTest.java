package com.silenteight.serp.governance.qa.analysis.update;

import com.silenteight.serp.governance.qa.domain.DecisionService;
import com.silenteight.serp.governance.qa.domain.DecisionState;
import com.silenteight.serp.governance.qa.domain.dto.CreateDecisionRequest;
import com.silenteight.serp.governance.qa.domain.dto.UpdateDecisionRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;

import static com.rabbitmq.client.ConnectionFactoryConfigurator.USERNAME;
import static com.silenteight.serp.governance.qa.AlertFixture.ALERT_NAME;
import static com.silenteight.serp.governance.qa.DecisionFixture.*;
import static com.silenteight.serp.governance.qa.domain.DecisionLevel.ANALYSIS;
import static com.silenteight.serp.governance.qa.domain.DecisionLevel.VALIDATION;
import static com.silenteight.serp.governance.qa.domain.DecisionState.FAILED;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateAnalysisDecisionUseCaseTest {

  private static final OffsetDateTime CREATED_AT = OffsetDateTime.now();
  @Mock
  DecisionService decisionService;

  UpdateAnalysisDecisionUseCase useCase;

  @BeforeEach
  void setUp() {
    useCase = new UpdateAnalysisDecisionUseCase(decisionService);
  }

  @Test
  void activateForPassedWillOnlyUpdateAnalysis() {
    //given
    UpdateDecisionRequest request = getUpdateDecisionRequest(STATE_NEW, COMMENT_OK);
    //when
    useCase.activate(request);
    //then
    ArgumentCaptor<UpdateDecisionRequest> updateRequestCaptor =
        ArgumentCaptor.forClass(UpdateDecisionRequest.class);
    verify(decisionService, times(1))
        .updateDecision(updateRequestCaptor.capture());
    UpdateDecisionRequest updateDecisionRequest = updateRequestCaptor.getValue();
    assertThat(updateDecisionRequest.getAlertName()).isEqualTo(ALERT_NAME);
    assertThat(updateDecisionRequest.getState()).isEqualTo(STATE_NEW);
    assertThat(updateDecisionRequest.getComment()).isEqualTo(COMMENT_OK);
    assertThat(updateDecisionRequest.getLevel()).isEqualTo(ANALYSIS);
    assertThat(updateDecisionRequest.getCreatedBy()).isEqualTo(USERNAME);
    assertThat(updateDecisionRequest.getCreatedAt()).isEqualTo(CREATED_AT);
  }

  private UpdateDecisionRequest getUpdateDecisionRequest(DecisionState state, String comment) {
    return UpdateDecisionRequest.of(
        ALERT_NAME,
        state,
        LEVEL_ANALYSIS,
        comment,
        USERNAME,
        CREATED_AT);
  }

  @Test
  void activateForFailedAnalysisWillUpdateAnalysisAndCreateNewValidation() {
    //given
    UpdateDecisionRequest request = getUpdateDecisionRequest(STATE_FAILED, COMMENT_FAILED);
    //when
    useCase.activate(request);
    //then
    ArgumentCaptor<UpdateDecisionRequest> updateRequestCaptor =
        ArgumentCaptor.forClass(UpdateDecisionRequest.class);
    ArgumentCaptor<CreateDecisionRequest> createRequestCaptor =
        ArgumentCaptor.forClass(CreateDecisionRequest.class);
    verify(decisionService, times(1))
        .updateDecision(updateRequestCaptor.capture());
    UpdateDecisionRequest updateDecisionRequest = updateRequestCaptor.getValue();
    assertThat(updateDecisionRequest.getAlertName()).isEqualTo(ALERT_NAME);
    assertThat(updateDecisionRequest.getState()).isEqualTo(FAILED);
    assertThat(updateDecisionRequest.getComment()).isEqualTo(COMMENT_FAILED);
    assertThat(updateDecisionRequest.getLevel()).isEqualTo(ANALYSIS);
    assertThat(updateDecisionRequest.getCreatedBy()).isEqualTo(USERNAME);
    assertThat(updateDecisionRequest.getCreatedAt()).isEqualTo(CREATED_AT);

    verify(decisionService, times(1))
        .createDecision(createRequestCaptor.capture());

    CreateDecisionRequest createDecisionRequest = createRequestCaptor.getValue();
    assertThat(createDecisionRequest.getAlertName()).isEqualTo(ALERT_NAME);
    assertThat(createDecisionRequest.getState()).isEqualTo(STATE_NEW);
    assertThat(createDecisionRequest.getLevel()).isEqualTo(VALIDATION);
    assertThat(createDecisionRequest.getCreatedBy()).isEqualTo(USERNAME);
    assertThat(createDecisionRequest.getCreatedAt()).isEqualTo(CREATED_AT);
  }
}
