package com.silenteight.sep.base.common.messaging.metrics;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;

import com.silenteight.sep.base.common.messaging.metrics.Size.SizeUnit;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import javax.validation.constraints.NotNull;

@ConfigurationProperties(prefix = "serp.messaging.metrics")
@Data
class MessagingMetricsProperties {

  private boolean enabled = true;

  @NotNull
  private SizeDistributionProperties size = SizeDistributionProperties.builder().build();

  @Data
  @Builder
  static class SizeDistributionProperties {

    @Default
    @NotNull
    Size.SizeUnit unit = SizeUnit.BYTES;

    @Default
    @NotNull
    long[] slaBoundaries = new long[]{
        32, 128, 256, 512, 1024, 2048, 10240, 20480
    };

    @Default
    @NotNull
    Duration bucketExpiry = Duration.ofMinutes(10);
  }
}
