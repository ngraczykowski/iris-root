package com.silenteight.scb.ingest.adapter.incomming.common.order;

import lombok.Data;

import com.silenteight.scb.ingest.adapter.incomming.common.validation.OracleRelationName;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties("serp.scb.bridge.alert-order")
@Component
@Data
@Validated
public class ScbBridgeAlertOrderProperties {

  @OracleRelationName
  private String dbRelationName = "SENS_V_FFF_RECORDS";

  @OracleRelationName
  private String cbsHitsDetailsHelperViewName;
}