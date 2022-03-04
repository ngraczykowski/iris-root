package com.silenteight.agent.common.metrics;

import com.silenteight.agent.common.metrics.micrometer.MicrometerRecorder;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import static org.aspectj.lang.Aspects.aspectOf;

@EnableAspectJAutoProxy(proxyTargetClass = true)
@ConditionalOnProperty(value = "agents.commons.metrics.enabled", havingValue = "true")
@Configuration
public class MetricsConfiguration {

  @Bean
  public MicrometerRecorder micrometerRecorder(
      MeterRegistry meterRegistry, @Value("${agents.commons.metrics.prefix}") String prefix) {
    return new MicrometerRecorder(meterRegistry, prefix);
  }

  @Bean
  public MetricsAspect theAspect() {
    return aspectOf(MetricsAspect.class);
  }
}
