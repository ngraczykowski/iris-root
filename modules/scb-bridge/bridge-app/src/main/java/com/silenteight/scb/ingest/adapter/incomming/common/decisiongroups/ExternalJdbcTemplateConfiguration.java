package com.silenteight.scb.ingest.adapter.incomming.common.decisiongroups;

import lombok.RequiredArgsConstructor;

import com.silenteight.scb.ingest.adapter.incomming.common.mode.OnAlertProcessorCondition;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.JdbcProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
@Conditional(OnAlertProcessorCondition.class)
class ExternalJdbcTemplateConfiguration {

  private final JdbcProperties properties;

  @Bean
  JdbcTemplate externalJdbcTemplate(
      @Qualifier("externalDataSource") DataSource externalDataSource) {

    JdbcTemplate template = new JdbcTemplate();

    template.setDataSource(externalDataSource);
    template.setLazyInit(true);
    template.setFetchSize(properties.getTemplate().getFetchSize());
    template.afterPropertiesSet();

    return template;
  }
}
