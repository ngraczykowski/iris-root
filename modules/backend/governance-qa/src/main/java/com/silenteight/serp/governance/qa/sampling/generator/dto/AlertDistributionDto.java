package com.silenteight.serp.governance.qa.sampling.generator.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.util.List;

@Builder
@Data
public class AlertDistributionDto {

  @NonNull
  Integer alertsCount;
  @NonNull
  List<DistributionDto> distributions;
}
