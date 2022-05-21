package com.silenteight.hsbc.bridge.metrics;


import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer.Builder;
import io.micrometer.core.instrument.config.MeterFilter;
import net.devh.boot.grpc.client.metric.MetricCollectingClientInterceptor;
import net.devh.boot.grpc.server.metric.MetricCollectingServerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
class MetricsConfiguration {

  @Bean
  MeterFilter filterMetricsTags() {
    return MeterFilter.ignoreTags("class");
  }

  @Bean
  TimedAspect timedAspect(MeterRegistry registry) {
    return new TimedAspect(registry);
  }

  @Bean
  MetricCollectingServerInterceptor metricCollectingServerInterceptor(MeterRegistry registry) {
    return new MetricCollectingServerInterceptor(
        registry,
        counter -> counter,
        Builder::publishPercentileHistogram
    );
  }

  @Bean
  MetricCollectingClientInterceptor metricCollectingClientInterceptor(MeterRegistry registry) {
    return new MetricCollectingClientInterceptor(
        registry,
        counter -> counter,
        Builder::publishPercentileHistogram
    );
  }
}
