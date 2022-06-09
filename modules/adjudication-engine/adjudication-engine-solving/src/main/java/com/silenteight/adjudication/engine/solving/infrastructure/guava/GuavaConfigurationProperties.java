/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.adjudication.engine.solving.infrastructure.guava;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix = "ae.solving.in-memory.guava")
public class GuavaConfigurationProperties {

  private int initialQueueCapacity = 100_000;
  private Duration ttlMinutes = Duration.ofMinutes(60 * 3);
  private AlertEvictionConfig alertEvictionConfig = AlertEvictionConfig.common();

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class AlertEvictionConfig {

    private int maxCapacity;

    static AlertEvictionConfig common() {
      return new AlertEvictionConfig(300000);
    }
  }
}
