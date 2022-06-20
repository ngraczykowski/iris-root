/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.gateway;

import lombok.RequiredArgsConstructor;

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.mode.OnAlertProcessorCondition;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.batch.ScbBridgeConfigProperties;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

import static java.util.Objects.nonNull;

@Configuration
@RequiredArgsConstructor
@Conditional(OnAlertProcessorCondition.class)
class CbsAckGatewayConfiguration {

  private final ScbBridgeConfigProperties scbBridgeConfigProperties;
  private final CbsConfigProperties cbsConfigProperties;
  private final ApplicationEventPublisher eventPublisher;

  @Bean
  CbsAckGateway cbsAckGateway(@Qualifier("externalDataSource") DataSource externalDataSource) {
    var jdbcTemplate = new JdbcTemplate(externalDataSource);
    jdbcTemplate.setQueryTimeout(scbBridgeConfigProperties.getQueryTimeout());

    var cbsAckGateway = new CbsAckGateway(
        cbsConfigProperties.getAckFunctionName(),
        jdbcTemplate,
        cbsConfigProperties.getSourceApplicationValues());

    if (nonNull(eventPublisher))
      cbsAckGateway.setEventPublisher(eventPublisher);

    return cbsAckGateway;
  }
}
