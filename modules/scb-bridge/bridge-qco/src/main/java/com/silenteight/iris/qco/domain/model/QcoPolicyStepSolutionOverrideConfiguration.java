/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.qco.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;

@Getter
@NoArgsConstructor
@AllArgsConstructor
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
