package com.silenteight.serp.governance.model.used;

import com.silenteight.serp.governance.model.domain.dto.ModelDto;
import com.silenteight.serp.governance.model.get.ModelDetailsQuery;
import com.silenteight.serp.governance.policy.details.PolicyDetailsQuery;
import com.silenteight.serp.governance.policy.domain.PolicyService;
import com.silenteight.serp.governance.policy.domain.dto.MarkPolicyAsUsedRequest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.silenteight.serp.governance.model.fixture.ModelFixtures.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MarkModelUsedOnProductionUseCaseTest {

  @Mock
  private ModelDetailsQuery modelDetailsQuery;
  @Mock
  private PolicyDetailsQuery policyDetailsQuery;
  @Mock
  private PolicyService policyService;
  @Mock
  private SendModelUsedOnProductionUseCase sendModelUsedOnProductionUseCase;

  @InjectMocks
  private MarkModelAsUsedOnProductionUseCase underTest;

  @Test
  void useModel() {
    // given
    when(modelDetailsQuery.get(MODEL_ID)).thenReturn(MODEL_DTO);
    when(policyDetailsQuery.details(POLICY_ID)).thenReturn(POLICY_DTO);
    // when
    underTest.applyByName(MODEL_RESOURCE_NAME);
    // then
    ArgumentCaptor<MarkPolicyAsUsedRequest> argumentCaptor = ArgumentCaptor
        .forClass(MarkPolicyAsUsedRequest.class);
    ArgumentCaptor<ModelDto> modelDtoArgumentCaptor = ArgumentCaptor
        .forClass(ModelDto.class);

    verify(policyService).markPolicyAsUsed(argumentCaptor.capture());
    verify(sendModelUsedOnProductionUseCase).activate(modelDtoArgumentCaptor.capture());
    MarkPolicyAsUsedRequest request = argumentCaptor.getValue();
    assertThat(request.getPolicyId()).isEqualTo(POLICY_ID);
    assertThat(request.getActivatedBy()).isEqualTo(UPDATED_BY);
    assertThat(modelDtoArgumentCaptor.getValue()).isEqualTo(MODEL_DTO);
  }
}
