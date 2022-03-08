package com.silenteight.scb.ingest.adapter.incomming.common.config;

import lombok.RequiredArgsConstructor;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.transaction.TransactionManagerCustomizers;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
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
  DataSourceTransactionManager externalTransactionManager() {
    return createTransactionManager(externalDataSource());
  }

  @Bean
  @ConfigurationProperties(prefix = "spring.external.datasource.hikari")
  DataSource externalDataSource() {
    return createHikariDataSource(externalDataSourceProperties());
  }

  @Bean
  @ConfigurationProperties(prefix = "spring.external.datasource")
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
  DataSourceTransactionManager externalOnDemandTransactionManager() {
    return createTransactionManager(externalOnDemandDataSource());
  }

  @Bean
  @ConfigurationProperties(prefix = "spring.external.datasource.hikari-on-demand")
  DataSource externalOnDemandDataSource() {
    return createHikariDataSource(externalDataSourceProperties());
  }
}
