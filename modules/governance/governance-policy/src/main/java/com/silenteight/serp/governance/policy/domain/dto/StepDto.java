package com.silenteight.serp.governance.policy.domain.dto;

import lombok.*;

import com.silenteight.serp.governance.policy.domain.StepType;

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
}
