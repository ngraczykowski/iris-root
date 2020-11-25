package com.silenteight.serp.governance.migrate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import com.silenteight.proto.serp.v1.alert.VectorValue;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({ "branchId", "matchGroupId", "solution", "enabled", "featureValues",
                     "comment" })
class BranchMigration {

  public static final Boolean DEFAULT_ENABLED = Boolean.TRUE;

  @NonNull
  private Long branchId;

  @NonNull
  private Long matchGroupId;

  @NonNull
  private String solution;

  private boolean enabled = DEFAULT_ENABLED;

  @NonNull
  private List<String> featureValues;

  @JsonIgnore
  List<VectorValue> getVectorValues() {
    return featureValues
        .stream()
        .map(textValue -> VectorValue.newBuilder().setTextValue(textValue).build())
        .collect(toList());
  }
}
