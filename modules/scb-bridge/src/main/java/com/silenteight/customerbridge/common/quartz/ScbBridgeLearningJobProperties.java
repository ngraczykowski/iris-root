package com.silenteight.customerbridge.common.quartz;

import lombok.Data;

import com.silenteight.customerbridge.common.validation.OracleRelationName;

import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Data
@Validated
class ScbBridgeLearningJobProperties {

  @NotBlank
  private String cronExpression;

  private boolean enabled;

  @OracleRelationName
  private String dbRelationName = "SENS_V_FFF_RECORDS";

  @OracleRelationName
  private String cbsHitsDetailsHelperViewName;

  private boolean useDelta = true;

}
