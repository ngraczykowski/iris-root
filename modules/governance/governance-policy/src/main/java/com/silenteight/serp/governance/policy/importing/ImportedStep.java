package com.silenteight.serp.governance.policy.importing;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import com.silenteight.serp.governance.policy.domain.StepType;
import com.silenteight.solving.api.v1.FeatureVectorSolution;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({
    "solution",
    "stepId",
    "stepName",
    "stepDescription",
    "stepType",
    "featuresLogic"
})
class ImportedStep {

  @NonNull
  private FeatureVectorSolution solution;

  private UUID stepId;

  @NonNull
  @JsonProperty("stepName")
  private String name;

  @NonNull
  @JsonProperty("stepDescription")
  private String description;

  @NonNull
  @JsonProperty("stepType")
  private StepType type;

  @NonNull
  @JsonProperty("featuresLogic")
  private List<ImportedFeatureLogic> featureLogics;
}
