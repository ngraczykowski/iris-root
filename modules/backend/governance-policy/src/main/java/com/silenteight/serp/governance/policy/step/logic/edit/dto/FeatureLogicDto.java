package com.silenteight.serp.governance.policy.step.logic.edit.dto;

import lombok.*;
import lombok.Builder.Default;

import java.util.Collection;
import javax.validation.Valid;
import javax.validation.constraints.Min;

import static java.util.List.of;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeatureLogicDto {

  @Min(1)
  private int toFulfill;

  @NonNull
  @Default
  @Valid
  private Collection<MatchConditionDto> features = of();
}
