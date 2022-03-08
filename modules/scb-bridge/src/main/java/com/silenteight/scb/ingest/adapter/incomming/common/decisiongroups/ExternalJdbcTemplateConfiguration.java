package com.silenteight.scb.ingest.adapter.incomming.common.decisiongroups;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.JdbcProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
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
