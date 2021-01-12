package com.silenteight.serp.governance.policy.domain.dto;

import lombok.*;
import lombok.Builder.Default;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.UUID;

import static java.util.List.of;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PolicyDto {

  private long id;
  @NonNull
  private UUID policyId;
  @NonNull
  private String name;
  @NonNull
  private State state;
  @NonNull
  private OffsetDateTime createdAt;
  @NonNull
  private String createdBy;
  @NonNull
  private OffsetDateTime updatedAt;
  @NonNull
  private String updatedBy;
  @NonNull
  @Default
  private Collection<StepDto> steps = of();
}
