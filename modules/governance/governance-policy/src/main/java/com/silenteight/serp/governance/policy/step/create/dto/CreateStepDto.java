package com.silenteight.serp.governance.policy.step.create.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import com.silenteight.serp.governance.policy.domain.dto.Solution;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateStepDto {

  @NonNull
  private UUID id;
  @NonNull
  private String name;
  private String description;
  @NonNull
  private Solution solution;
}
