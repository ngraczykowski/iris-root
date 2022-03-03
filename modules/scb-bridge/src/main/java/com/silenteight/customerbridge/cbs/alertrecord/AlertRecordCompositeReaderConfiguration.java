package com.silenteight.customerbridge.cbs.alertrecord;

import lombok.RequiredArgsConstructor;

import com.silenteight.customerbridge.cbs.batch.ScbBridgeConfigProperties;
import com.silenteight.customerbridge.common.batch.DateConverter;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
class AlertRecordCompositeReaderConfiguration {

  private final ScbBridgeConfigProperties configProperties;
  private final GnsSolutionMapper gnsSolutionMapper;

  @Bean
  DatabaseAlertRecordCompositeReader alertRecordCompositeReader(
      @Qualifier("externalDataSource") DataSource externalDataSource) {

    var jdbcTemplate = new JdbcTemplate(externalDataSource);
    jdbcTemplate.setQueryTimeout(configProperties.getQueryTimeout());

    return new DatabaseAlertRecordCompositeReader(
        new DatabaseAlertRecordReader(jdbcTemplate),
        new DatabaseDecisionRecordReader(jdbcTemplate, decisionRecordRowMapper()),
        new DatabaseCbsHitDetailsReader(jdbcTemplate));
  }

  private DecisionRecordRowMapper decisionRecordRowMapper() {
    var dateConverter = new DateConverter(configProperties.getTimeZone());

    return new DecisionRecordRowMapper(dateConverter, gnsSolutionMapper);
  }
}
