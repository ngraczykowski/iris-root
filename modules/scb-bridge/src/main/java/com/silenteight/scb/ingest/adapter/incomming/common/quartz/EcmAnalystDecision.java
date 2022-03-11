package com.silenteight.scb.ingest.adapter.incomming.common.quartz;

import lombok.Data;

import com.silenteight.scb.ingest.adapter.incomming.common.model.decision.Decision.AnalystSolution;

import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Data
@Validated
public class EcmAnalystDecision {

  @NotBlank
  private String text;

  private AnalystSolution solution;
}
