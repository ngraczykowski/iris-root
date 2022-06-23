package com.silenteight.serp.governance.qa.sampling.generator.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Builder
@Data
public class DistributionDto {

  @NonNull
  String fieldName;
  @NonNull
  String fieldValue;
}
