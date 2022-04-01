package com.silenteight.qco.domain.model;

import lombok.Getter;

import com.fasterxml.jackson.annotation.JsonProperty;

@Getter
public class QcoPolicyStepSolutionOverrideConfiguration {

  @JsonProperty(index = 0)
  private String policyId;
  @JsonProperty(index = 1)
  private String stepId;
  @JsonProperty(index = 2)
  private Long matchThreshold;
  @JsonProperty(index = 3)
  private String solution;
  @JsonProperty(index = 4)
  private String solutionOverride;

}
