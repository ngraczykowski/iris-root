package com.silenteight.serp.governance.qa.manage.validation.update;

import com.silenteight.serp.governance.qa.manage.domain.DecisionService;
import com.silenteight.serp.governance.qa.manage.domain.dto.UpdateDecisionRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;

import static com.rabbitmq.client.ConnectionFactoryConfigurator.USERNAME;
import static com.silenteight.serp.governance.qa.AlertFixture.ALERT_NAME;
import static com.silenteight.serp.governance.qa.DecisionFixture.COMMENT_OK;
import static com.silenteight.serp.governance.qa.DecisionFixture.LEVEL_VALIDATION;
import static com.silenteight.serp.governance.qa.DecisionFixture.STATE_NEW;
import static com.silenteight.serp.governance.qa.manage.domain.DecisionLevel.VALIDATION;
import static java.time.OffsetDateTime.now;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateValidationDecisionUseCaseTest {

  private static final OffsetDateTime CREATED_AT = now();
  @Mock
  DecisionService decisionService;

  UpdateValidationDecisionUseCase useCase;

  @BeforeEach
  void setUp() {
    useCase = new UpdateValidationDecisionUseCase(decisionService);
  }

  @Test
  void activateWillUpdateValidation() {
    //given
    UpdateDecisionRequest request = UpdateDecisionRequest.of(
        ALERT_NAME,
        STATE_NEW,
        LEVEL_VALIDATION,
        COMMENT_OK,
        USERNAME,
        CREATED_AT);
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
    assertThat(updateDecisionRequest.getLevel()).isEqualTo(VALIDATION);
    assertThat(updateDecisionRequest.getCreatedBy()).isEqualTo(USERNAME);
    assertThat(updateDecisionRequest.getCreatedAt()).isEqualTo(CREATED_AT);
  }
}
