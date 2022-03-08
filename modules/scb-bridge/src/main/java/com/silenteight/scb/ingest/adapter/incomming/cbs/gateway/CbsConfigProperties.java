package com.silenteight.scb.ingest.adapter.incomming.cbs.gateway;

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

  @NonNull
  private SourceApplicationValues sourceApplicationValues = new SourceApplicationValues();
}
