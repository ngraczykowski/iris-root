/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.adjudication.engine.solving.infrastructure.hazelcast;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.hazelcast.config.EvictionConfig;
import com.hazelcast.config.EvictionPolicy;
import com.hazelcast.config.MaxSizePolicy;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

import static com.hazelcast.config.EvictionPolicy.LRU;
import static com.hazelcast.config.MaxSizePolicy.USED_HEAP_SIZE;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix = "ae.solving.in-memory.hazelcast")
public class HazelcastConfigurationProperties {

  // Use this name to make cluster isolation in case no network isolation available
  private String clusterName;

  private AsyncBackup asyncBackup = AsyncBackup.disabled();
  private boolean enableReadBackup = true;
  private Duration ttlMinutes = Duration.ofMinutes(60 * 3);
  private AlertEvictionConfig alertEvictionConfig = AlertEvictionConfig.common();

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class AsyncBackup {
    private int minAsyncBackups = 0;

    static AsyncBackup disabled() {
      return new AsyncBackup(0);
    }
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class AlertEvictionConfig {

    private MaxSizePolicy maxSizePolicy;
    private int maxCapacity;
    private EvictionPolicy evictionPolicy;

    static AlertEvictionConfig common() {
      return new AlertEvictionConfig(USED_HEAP_SIZE, 1024, LRU);
    }

    EvictionConfig getEvictionConfig() {
      return new EvictionConfig()
          .setMaxSizePolicy(this.maxSizePolicy)
          .setSize(this.maxCapacity)
          .setEvictionPolicy(this.evictionPolicy);
    }
  }
}
