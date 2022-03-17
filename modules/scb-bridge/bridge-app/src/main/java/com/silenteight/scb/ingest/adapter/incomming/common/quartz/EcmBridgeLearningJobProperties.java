package com.silenteight.scb.ingest.adapter.incomming.common.quartz;

import lombok.Data;

import com.silenteight.scb.ingest.adapter.incomming.common.validation.OracleRelationName;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import javax.validation.constraints.NotBlank;

@ConfigurationProperties("serp.scb.bridge.learning.ecm")
@Component
@Data
@Validated
public class EcmBridgeLearningJobProperties {

  private String deltaJobName = "ECM_LEARNING";

  @NotBlank
  private String cronExpression;

  private boolean useDelta = true;

  private boolean enabled;

  private String ecmViewName;

  private List<EcmAnalystDecision> decisions;

  @OracleRelationName
  private String dbRelationName = "SENS_V_FFF_RECORDS";

  @OracleRelationName
  private String cbsHitsDetailsHelperViewName;
}
