package com.silenteight.serp.governance.policy.step.logic.edit.dto;

import lombok.*;

import com.silenteight.serp.governance.policy.domain.Condition;

import java.util.Collection;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import static com.silenteight.serp.governance.policy.domain.DomainConstants.MAX_FEATURE_NAME_LENGTH;
import static com.silenteight.serp.governance.policy.domain.DomainConstants.MIN_FEATURE_NAME_LENGTH;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchConditionDto {

  @NonNull
  @Size(min = MIN_FEATURE_NAME_LENGTH, max = MAX_FEATURE_NAME_LENGTH)
  private String name;
  @NonNull
  private Condition condition;
  @NonNull
  @NotEmpty
  private Collection<String> values;
}
