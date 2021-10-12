package com.silenteight.serp.governance.qa.retention.personalinformation;

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
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EraseDecisionCommentUseCaseTest {

  private static final String ALERT_DISCRIMINATOR = "54673323-7df0-484a-92d2-4a9c4ec6c55a";

  private EraseDecisionCommentUseCase underTest;

  @Mock
  private DecisionService decisionService;

  @BeforeEach
  void setUp() {
    underTest = new EraseDecisionCommentUseCase(decisionService);
  }

  @Test
  void activateShouldEraseOneDecisionComment() {
    //given
    List<String> alerts = List.of(ALERT_DISCRIMINATOR);
    ArgumentCaptor<EraseDecisionCommentRequest> eraseDecisionCommentRequestCaptor =
        ArgumentCaptor.forClass(EraseDecisionCommentRequest.class);
    //when
    underTest.activate(alerts);
    //then
    verify(decisionService,  times(1))
        .eraseComment(eraseDecisionCommentRequestCaptor.capture());
    assertThat(eraseDecisionCommentRequestCaptor.getValue().getDiscriminator())
        .isEqualTo(ALERT_DISCRIMINATOR);
    assertThat(eraseDecisionCommentRequestCaptor.getValue().getLevel())
        .isEqualTo(ANALYSIS);
    assertThat(eraseDecisionCommentRequestCaptor.getValue().getCreatedBy())
        .isEqualTo(PRINCIPAL_NAME);
    assertThat(eraseDecisionCommentRequestCaptor.getValue().getCorrelationId())
        .isNotNull();
    assertThat(eraseDecisionCommentRequestCaptor.getValue().getCreatedAt()).isNotNull();
  }
}
