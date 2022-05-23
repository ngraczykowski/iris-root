package com.silenteight.scb.ingest.adapter.incomming.common.config;

import lombok.RequiredArgsConstructor;

import com.silenteight.scb.ingest.adapter.incomming.common.mode.OnAlertProcessorCondition;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.transaction.TransactionManagerCustomizers;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class SyncDataSourcesConfiguration {

  private final ObjectProvider<TransactionManagerCustomizers> transactionManagerCustomizers;

  @Bean
  @Primary
  @ConfigurationProperties(prefix = "spring.datasource.hikari")
  HikariDataSource dataSource() {
    return createHikariDataSource(dataSourceProperties());
  }

  @Bean
  @Primary
  @ConfigurationProperties(prefix = "spring.datasource")
  DataSourceProperties dataSourceProperties() {
    return new DataSourceProperties();
  }

  private static HikariDataSource createHikariDataSource(DataSourceProperties properties) {
    return properties
        .initializeDataSourceBuilder()
        .type(HikariDataSource.class)
        .build();
  }

  @Bean
  @Conditional(OnAlertProcessorCondition.class)
  DataSourceTransactionManager externalTransactionManager() {
    return createTransactionManager(externalDataSource());
  }

  @Bean
  @ConfigurationProperties(prefix = "spring.external.datasource.hikari")
  @Conditional(OnAlertProcessorCondition.class)
  DataSource externalDataSource() {
    return createHikariDataSource(externalDataSourceProperties());
  }

  @Bean
  @ConfigurationProperties(prefix = "spring.external.datasource")
  @Conditional(OnAlertProcessorCondition.class)
  DataSourceProperties externalDataSourceProperties() {
    return new DataSourceProperties();
  }

  private DataSourceTransactionManager createTransactionManager(DataSource dataSource) {
    DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(
        dataSource);

    transactionManagerCustomizers.ifAvailable(c -> c.customize(transactionManager));

    return transactionManager;
  }

  @Bean
  @Conditional(OnAlertProcessorCondition.class)
  DataSourceTransactionManager externalOnDemandTransactionManager() {
    return createTransactionManager(externalOnDemandDataSource());
  }

  @Bean
  @ConfigurationProperties(prefix = "spring.external.datasource.hikari-on-demand")
  @Conditional(OnAlertProcessorCondition.class)
  DataSource externalOnDemandDataSource() {
    return createHikariDataSource(externalDataSourceProperties());
  }
}
