package com.silenteight.customerbridge.common.recommendation;

import lombok.Data;

import com.silenteight.customerbridge.common.validation.OracleRelationName;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties("serp.scb.bridge.recommendation")
@Component
@Data
@Validated
public class RecommendationOrderProperties {

  @OracleRelationName
  private String dbRelationName = "SENS_V_FFF_RECORDS";

  @OracleRelationName
  private String cbsHitsDetailsHelperViewName;

  private long timeout = 30000;

  private boolean onDemandEnabled;

  private String outdatedMessage =
      "PLEASE NOTE: THE RISK EVENT DETAILS MAY HAVE BEEN UPDATED,"
          + " PLEASE REVIEW ALL 'RISK EVENTS' ACCORDINGLY.";
}