package com.silenteight.serp.governance.policy.domain.dto;

import lombok.*;

import com.silenteight.serp.governance.policy.domain.Condition;

import java.util.Collection;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchConditionConfigurationDto {

  @NonNull
  private String name;
  @NonNull
  private Condition condition;
  @NonNull
  private Collection<String> values;
}
