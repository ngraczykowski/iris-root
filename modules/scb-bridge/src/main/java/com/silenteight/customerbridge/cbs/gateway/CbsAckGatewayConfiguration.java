package com.silenteight.customerbridge.cbs.gateway;

import lombok.RequiredArgsConstructor;

import com.silenteight.customerbridge.cbs.batch.ScbBridgeConfigProperties;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

import static java.util.Objects.nonNull;

@Configuration
@RequiredArgsConstructor
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
