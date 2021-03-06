package com.silenteight.serp.governance.policy.step.logic.edit.dto;

import lombok.*;
import lombok.Builder.Default;

import java.util.Collection;
import javax.validation.Valid;

import static java.util.List.of;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EditStepLogicDto {

  @NonNull
  @Default
  @Valid
  private Collection<FeatureLogicDto> featuresLogic = of();
}
