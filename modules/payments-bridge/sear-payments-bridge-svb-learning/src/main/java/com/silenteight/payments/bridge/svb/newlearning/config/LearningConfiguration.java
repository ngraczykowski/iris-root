package com.silenteight.payments.bridge.svb.newlearning.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.common.app.LearningProperties;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
@Slf4j
@EnableConfigurationProperties(LearningProperties.class)
class LearningConfiguration {
}
