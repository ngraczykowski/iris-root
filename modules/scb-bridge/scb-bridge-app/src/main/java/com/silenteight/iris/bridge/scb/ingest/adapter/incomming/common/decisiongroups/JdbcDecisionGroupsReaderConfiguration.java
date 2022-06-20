/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.decisiongroups;

import lombok.RequiredArgsConstructor;

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.mode.OnAlertProcessorCondition;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
@EnableConfigurationProperties(JdbcDecisionGroupsReaderProperties.class)
@RequiredArgsConstructor
@Conditional(OnAlertProcessorCondition.class)
class JdbcDecisionGroupsReaderConfiguration {

  private final JdbcDecisionGroupsReaderProperties properties;

  @Bean
  JdbcDecisionGroupsReader jdbcDecisionGroupsReader(
      @Qualifier("externalJdbcTemplate") JdbcTemplate template) {

    return this.properties.setupReader(new JdbcDecisionGroupsReader(template));
  }
}
