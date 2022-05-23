package com.silenteight.payments.bridge.svb.learning.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.common.app.LearningProperties;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.AlternativeJdkIdGenerator;
import org.springframework.util.IdGenerator;

@RequiredArgsConstructor
@Configuration
@Slf4j
@EnableConfigurationProperties(LearningProperties.class)
class LearningConfiguration {

  @Bean
  IdGenerator idGenerator() {
    return new AlternativeJdkIdGenerator();
  }
}
