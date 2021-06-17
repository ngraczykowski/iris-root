package com.silenteight.serp.governance.policy.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import com.silenteight.serp.governance.policy.domain.Condition;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({ "name", "condition", "values" })
public class TransferredMatchConditionDto implements Serializable {

  private static final long serialVersionUID = -4831030905746132883L;

  @NonNull
  private String name;

  @NonNull
  private Condition condition;

  @NonNull
  private List<String> values;
}
