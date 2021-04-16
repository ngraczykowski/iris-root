package com.silenteight.serp.governance.model.create.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateModelDto {

  @NonNull
  private UUID id;
  @NonNull
  private String policyName;
}
