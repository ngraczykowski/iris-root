package com.silenteight.serp.governance.policy.details;

import com.silenteight.serp.governance.policy.details.dto.PolicyDetailsDto;
import com.silenteight.serp.governance.policy.domain.PolicyState;
import com.silenteight.serp.governance.policy.domain.dto.PolicyDto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.UUID;

import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PolicyDetailsUseCaseTest {

  private static final String RESOURCE_NAME = "policies/b4708d8c-4832-6fde-8dc0-d17b4708d8ca";
  private static final String POLICY_NAME = "TEST_POLICY";
  private static final OffsetDateTime CREATED_AT = OffsetDateTime.now().minusHours(1);
  private static final OffsetDateTime UPDATED_AT = OffsetDateTime.now();
  private static final String CREATED_BY = "USER";
  private static final String UPDATED_BY = "USER2";
  private static final UUID POLICY_UUID = randomUUID();
  private static final int STEPS_COUNT = 2;
  private static final PolicyDto POLICY_DTO = PolicyDto.builder()
      .id(POLICY_UUID)
      .name(RESOURCE_NAME)
      .policyName(POLICY_NAME)
      .state(PolicyState.SAVED)
      .createdAt(CREATED_AT)
      .updatedAt(UPDATED_AT)
      .createdBy(CREATED_BY)
      .updatedBy(UPDATED_BY)
      .build();
  private static final PolicyDetailsDto POLICY_DETAILS_DTO = PolicyDetailsDto.builder()
      .id(POLICY_UUID)
      .name(RESOURCE_NAME)
      .policyName(POLICY_NAME)
      .state(PolicyState.SAVED)
      .createdAt(CREATED_AT)
      .updatedAt(UPDATED_AT)
      .createdBy(CREATED_BY)
      .updatedBy(UPDATED_BY)
      .stepsCount(STEPS_COUNT)
      .build();

  @Mock
  private PolicyDetailsQuery policyDetailsQuery;
  @Mock
  private PolicyStepsCountQuery policyStepsCountQuery;

  private PolicyDetailsUseCase underTest;

  @BeforeEach
  void setUp() {
    underTest = new PolicyDetailsConfiguration().policyDetailsUseCase(
        policyDetailsQuery, policyStepsCountQuery);
  }

  @Test
  void createValidResponse() {
    when(policyDetailsQuery.details(POLICY_UUID)).thenReturn(POLICY_DTO);
    when(policyStepsCountQuery.getStepsCount(POLICY_UUID)).thenReturn(Long.valueOf(STEPS_COUNT));

    PolicyDetailsDto result = underTest.activate(POLICY_UUID);

    assertThat(result).isEqualTo(POLICY_DETAILS_DTO);
  }
}
