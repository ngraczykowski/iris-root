package com.silenteight.customerbridge.common.decisiongroups;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
@EnableConfigurationProperties(JdbcDecisionGroupsReaderProperties.class)
@RequiredArgsConstructor
class JdbcDecisionGroupsReaderConfiguration {

  private final JdbcDecisionGroupsReaderProperties properties;

  @Bean
  JdbcDecisionGroupsReader jdbcDecisionGroupsReader(
      @Qualifier("externalJdbcTemplate") JdbcTemplate template) {

    return this.properties.setupReader(new JdbcDecisionGroupsReader(template));
  }
}
