package com.silenteight.customerbridge.common.quartz;

import lombok.Data;

import com.silenteight.proto.serp.v1.alert.AnalystSolution;

import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Data
@Validated
public class EcmAnalystDecision {

  @NotBlank
  private String text;

  private AnalystSolution solution;
}
