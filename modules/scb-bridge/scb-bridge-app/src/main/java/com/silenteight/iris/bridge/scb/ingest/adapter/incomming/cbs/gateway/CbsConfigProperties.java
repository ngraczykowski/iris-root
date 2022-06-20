/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.gateway;

import lombok.Data;
import lombok.NonNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@ConfigurationProperties("serp.scb.bridge.cbs.config")
@Component
@Data
@Validated
public class CbsConfigProperties {

  @NotBlank
  private String ackFunctionName = "CBS_INTERFACE_PKG.F_CBS_S8_LOG_ACK";

  @NotBlank
  private String recomFunctionName = "CBS_INTERFACE_PKG.F_CBS_S8_LOG_RECOM";

  @NotBlank
  private String recomWithQcoFunctionName = "CBS_INTERFACE_PKG_V4.F_CBS_S8_LOG_RECOM";

  @NonNull
  private SourceApplicationValues sourceApplicationValues = new SourceApplicationValues();
}
