package com.silenteight.serp.governance.policy.step.logic.edit.dto;

import lombok.*;

import com.silenteight.serp.governance.policy.domain.Condition;

import java.util.Collection;
import javax.validation.constraints.Min;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchConditionDto {

  @NonNull
  private String name;
  @NonNull
  private Condition condition;
  @NonNull
  @Min(1)
  private Collection<String> values;
}
