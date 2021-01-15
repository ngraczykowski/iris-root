package com.silenteight.serp.governance.policy.importing;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import com.silenteight.serp.governance.policy.domain.Condition;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({ "name", "condition", "values" })
public class MatchCondition {

  @NonNull
  private String name;

  @NonNull
  private Condition condition;

  @NonNull
  private List<String> values;
}
