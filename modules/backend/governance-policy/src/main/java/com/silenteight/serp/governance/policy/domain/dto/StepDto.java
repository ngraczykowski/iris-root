package com.silenteight.serp.governance.policy.domain.dto;

import lombok.*;

import com.silenteight.serp.governance.policy.domain.StepType;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StepDto {

  @NonNull
  private UUID id;
  @NonNull
  private String name;
  @NonNull
  private StepType type;
  private String description;
  @NonNull
  private Solution solution;
  @NonNull
  private String createdBy;
  @NonNull
  private OffsetDateTime createdAt;
  private String updatedBy;
  private OffsetDateTime updatedAt;
}
