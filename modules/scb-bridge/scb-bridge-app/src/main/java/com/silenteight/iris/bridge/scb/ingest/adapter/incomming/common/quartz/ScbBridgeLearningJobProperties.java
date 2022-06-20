/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.quartz;

import lombok.Data;

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.validation.OracleRelationName;

import org.springframework.validation.annotation.Validated;

@Data
@Validated
class ScbBridgeLearningJobProperties {

  private String cronExpression;

  private boolean enabled;

  @OracleRelationName
  private String dbRelationName = "SENS_V_FFF_RECORDS";

  @OracleRelationName
  private String cbsHitsDetailsHelperViewName;

  private boolean useDelta = true;

}
