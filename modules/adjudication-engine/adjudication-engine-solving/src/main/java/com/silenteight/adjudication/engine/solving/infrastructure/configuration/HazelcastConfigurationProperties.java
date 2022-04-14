package com.silenteight.adjudication.engine.solving.infrastructure.configuration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HazelcastConfigurationProperties {
  private AsyncBackup asyncBackup = AsyncBackup.disabled();
  private boolean enableReadBackup = true;
  private Duration ttl = Duration.ofSeconds(30);

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class AsyncBackup {
    private int minAsyncBackups = 0;

    static AsyncBackup disabled() {
      return new AsyncBackup(0);
    }
  }
}
