package com.silenteight.sens.webapp.aspects.metrics;

import lombok.RequiredArgsConstructor;

import io.micrometer.core.instrument.binder.MeterBinder;
import org.aspectj.lang.Aspects;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@ConditionalOnClass(name = "io.micrometer.core.instrument.MeterRegistry")
@Configuration
@RequiredArgsConstructor
public class MetricAspectsAutoConfiguration {

  @Bean
  public TimedMethodAspect timedMethodAspect() {
    return Aspects.aspectOf(TimedMethodAspect.class);
  }

  @Bean
  public MeterBinder timedMethodAspectMeterBinder(TimedMethodAspect aspect) {
    return aspect::setRegistry;
  }
}
