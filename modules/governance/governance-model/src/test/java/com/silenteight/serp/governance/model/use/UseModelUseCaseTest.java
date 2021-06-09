package com.silenteight.serp.governance.model.use;

import com.silenteight.serp.governance.model.get.ModelDetailsQuery;
import com.silenteight.serp.governance.policy.details.PolicyDetailsQuery;
import com.silenteight.serp.governance.policy.domain.PolicyService;
import com.silenteight.serp.governance.policy.domain.dto.UsePolicyRequest;

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
class UseModelUseCaseTest {

  @Mock
  private ModelDetailsQuery modelDetailsQuery;
  @Mock
  private PolicyDetailsQuery policyDetailsQuery;
  @Mock
  private PolicyService policyService;

  @InjectMocks
  private UseModelUseCase underTest;

  @Test
  void createModel() {
    // given
    when(modelDetailsQuery.get(MODEL_ID)).thenReturn(MODEL_DTO);
    when(policyDetailsQuery.details(POLICY_ID)).thenReturn(POLICY_DTO);

    // when
    underTest.apply(MODEL_RESOURCE_NAME);

    // then
    ArgumentCaptor<UsePolicyRequest> argumentCaptor = ArgumentCaptor
        .forClass(UsePolicyRequest.class);
    verify(policyService).usePolicy(argumentCaptor.capture());
    UsePolicyRequest usePolicyRequest = argumentCaptor.getValue();
    assertThat(usePolicyRequest.getPolicyId()).isEqualTo(POLICY_ID);
    assertThat(usePolicyRequest.getActivatedBy()).isEqualTo(UPDATED_BY);
  }
}
