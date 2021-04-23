package com.silenteight.serp.governance.model.fixture;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.serp.governance.model.domain.dto.ModelDto;

import java.time.OffsetDateTime;
import java.util.UUID;

import static com.silenteight.serp.governance.policy.current.CurrentPolicyFixture.CURRENT_POLICY_NAME;
import static java.time.ZoneOffset.UTC;
import static java.util.UUID.fromString;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ModelFixtures {

  public static final UUID MODEL_ID = fromString("d6fb8ae1-ab37-4622-935a-706ea6c53800");
  public static final String POLICY_NAME = "policies/b4708d8c-4832-6fde-8dc0-d17b4708d8ca";
  public static final String DEFAULT_MODEL_NAME = "models/default";
  public static final String MODEL_RESOURCE_NAME = "models/" + MODEL_ID.toString();
  public static final String MODEL_RESOURCE_DEFAULT_NAME = "models/default";
  public static final OffsetDateTime CREATED_AT =
      OffsetDateTime.of(2021, 03, 12, 11, 25, 10, 0, UTC);
  public static final String CREATED_BY = "asmith";

  public static final ModelDto MODEL_DTO =
      ModelDto.builder()
          .name(MODEL_RESOURCE_NAME)
          .policyName(POLICY_NAME)
          .createdAt(CREATED_AT)
          .build();

  public static final ModelDto DEFAULT_MODEL_DTO =
      ModelDto.builder()
          .name(MODEL_RESOURCE_DEFAULT_NAME)
          .policyName(CURRENT_POLICY_NAME)
          .createdAt(CREATED_AT)
          .build();
}
