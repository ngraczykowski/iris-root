package com.silenteight.customerbridge.cbs;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import static java.lang.Math.min;

@ConfigurationProperties("serp.scb.bridge.config")
@Component
@Data
@Validated
public class ScbBridgeConfigProperties {

  private static final int MAXIMUM_ORACLE_PAGE_SIZE = 1_000;

  @Min(1)
  @Max(100_000)
  private int chunkSize = 250;

  private int queryTimeout = 300;

  private String timeZone;

  public int getOraclePageSize() {
    return min(chunkSize, MAXIMUM_ORACLE_PAGE_SIZE);
  }
}