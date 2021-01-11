package com.silenteight.serp.governance.policy.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StepDto {

  private UUID id;
  private String name;
  private String type;
  private String description;
  private String solution;
}
