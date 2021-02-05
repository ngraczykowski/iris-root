package com.silenteight.serp.governance.policy.create.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import com.silenteight.serp.governance.policy.domain.PolicyState;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePolicyDto {

  @NonNull
  private UUID id;
  @NonNull
  private String name;
  @NonNull
  private PolicyState state;
}
