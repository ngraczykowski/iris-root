package com.silenteight.serp.governance.policy.domain.dto;

import lombok.*;

import com.silenteight.serp.governance.policy.domain.PolicyState;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PolicyDto {

  @NonNull
  private UUID id;
  @NonNull
  private String name;
  @NonNull
  private String policyName;
  private String description;
  @NonNull
  private OffsetDateTime createdAt;
  @NonNull
  private PolicyState state;
  @NonNull
  private String createdBy;
  private OffsetDateTime updatedAt;
  @NonNull
  private String updatedBy;
}
