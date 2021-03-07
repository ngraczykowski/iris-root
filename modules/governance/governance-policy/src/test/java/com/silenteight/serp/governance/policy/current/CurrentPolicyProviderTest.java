package com.silenteight.serp.governance.policy.current;

import com.silenteight.serp.governance.policy.solve.InUsePolicyQuery;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.silenteight.serp.governance.policy.current.CurrentPolicyFixture.CURRENT_POLICY_NAME;
import static com.silenteight.serp.governance.policy.current.CurrentPolicyFixture.CURRENT_POLICY_UUID;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CurrentPolicyProviderTest {

  @Mock
  private InUsePolicyQuery inUsePolicyQuery;

  @Test
  void shouldReturnCurrentPolicy() {
    when(inUsePolicyQuery.getPolicyInUse()).thenReturn(Optional.of(CURRENT_POLICY_UUID));
    CurrentPolicyProvider currentPolicyProvider = new CurrentPolicyProvider(inUsePolicyQuery);

    Optional<String> currentPolicy = currentPolicyProvider.getCurrentPolicy();

    assertThat(currentPolicy.get()).isEqualTo(CURRENT_POLICY_NAME);
  }

  @Test
  void shouldHandleMissingCurrentPolicy() {
    when(inUsePolicyQuery.getPolicyInUse()).thenReturn(Optional.empty());
    CurrentPolicyProvider currentPolicyProvider = new CurrentPolicyProvider(inUsePolicyQuery);

    Optional<String> currentPolicy = currentPolicyProvider.getCurrentPolicy();

    assertThat(currentPolicy).isEmpty();
  }
}