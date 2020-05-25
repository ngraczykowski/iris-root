package com.silenteight.serp.common.messaging.metrics;

import lombok.RequiredArgsConstructor;

import com.silenteight.serp.common.messaging.metrics.MessagingMetricsProperties.SizeDistributionProperties;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties(MessagingMetricsProperties.class)
@ConditionalOnClass(name = "org.springframework.amqp.rabbit.connection.CachingConnectionFactory")
public class MessagingMetricsConfiguration {

  @Bean
  @ConditionalOnProperty(
      prefix = "serp.messaging.metrics",
      name = "enabled",
      havingValue = "true",
      matchIfMissing = true)
  SizeDistributionMetricsMessageListener sizeDistributionMetricsMessageListener(
      MessagingMetricsProperties properties) {
    return new SizeDistributionMetricsMessageListener(
        createMetrics("serp.message.size.received", "Received message size", properties),
        createMetrics("serp.message.size.sent", "Sent message size", properties));
  }

  private static SizeDistributionMetrics createMetrics(
      String name, String description, MessagingMetricsProperties properties) {

    SizeDistributionProperties sizeDistributionProperties = properties.getSize();

    return SizeDistributionMetrics
        .builder()
        .name(name)
        .description(description)
        .unit(sizeDistributionProperties.getUnit())
        .slaBoundaries(sizeDistributionProperties.getSlaBoundaries())
        .bucketExpiry(sizeDistributionProperties.getBucketExpiry())
        .build();
  }
}
