package com.silenteight.serp.governance.model.fixture;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.sep.base.common.time.DigitsOnlyDateFormatter;
import com.silenteight.serp.governance.model.domain.dto.ModelDto;
import com.silenteight.serp.governance.policy.domain.dto.PolicyDto;

import java.time.OffsetDateTime;
import java.util.UUID;

import static com.silenteight.serp.governance.policy.current.CurrentPolicyFixture.CURRENT_POLICY_NAME;
import static com.silenteight.serp.governance.policy.domain.PolicyState.IN_USE;
import static java.time.ZoneOffset.UTC;
import static java.util.UUID.fromString;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ModelFixtures {

  public static final UUID POLICY_ID = fromString("b4708d8c-4832-6fde-8dc0-d17b4708d8ca");
  public static final UUID MODEL_ID = fromString("d6fb8ae1-ab37-4622-935a-706ea6c53800");
  public static final String POLICY_NAME = "policy";
  public static final String POLICY = "policies/" + POLICY_ID;
  public static final String DEFAULT_MODEL_NAME = "solvingModels/default";
  public static final String MODEL_RESOURCE_NAME = "solvingModels/" + MODEL_ID;
  public static final String MODEL_RESOURCE_DEFAULT_NAME = "solvingModels/default";
  public static final OffsetDateTime CREATED_AT =
      OffsetDateTime.of(2021, 3, 12, 11, 25, 10, 0, UTC);
  public static final String CREATED_BY = "asmith";
  public static final OffsetDateTime UPDATED_AT =
      OffsetDateTime.of(2021, 4, 11, 8, 10, 15, 0, UTC);
  public static final String UPDATED_BY = "jdoe";

  public static final ModelDto MODEL_DTO =
      ModelDto.builder()
          .name(MODEL_RESOURCE_NAME)
          .policy(POLICY)
          .createdAt(CREATED_AT)
          .modelVersion(DigitsOnlyDateFormatter.INSTANCE.format(CREATED_AT))
          .build();

  public static final ModelDto DEFAULT_MODEL_DTO =
      ModelDto.builder()
          .name(MODEL_RESOURCE_DEFAULT_NAME)
          .policy(CURRENT_POLICY_NAME)
          .createdAt(CREATED_AT)
          .modelVersion(DigitsOnlyDateFormatter.INSTANCE.format(CREATED_AT))
          .build();

  public static final PolicyDto POLICY_DTO = PolicyDto.builder()
      .id(POLICY_ID)
      .name(POLICY_NAME)
      .policyName(POLICY)
      .state(IN_USE)
      .createdAt(CREATED_AT)
      .updatedAt(UPDATED_AT)
      .createdBy(CREATED_BY)
      .updatedBy(UPDATED_BY)
      .build();
}
