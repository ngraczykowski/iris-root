package com.silenteight.scb.ingest.adapter.incomming.common.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.scb.ingest.adapter.incomming.common.domain.GnsSyncConstants;
import com.silenteight.scb.ingest.adapter.incomming.common.mode.OnAlertProcessorCondition;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.transaction.TransactionManagerCustomizers;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SyncDataSourcesConfiguration {

  private final ObjectProvider<TransactionManagerCustomizers> transactionManagerCustomizers;
  private final ApplicationContext context;

  @PostConstruct
  public void setTransactionManagerBeanPrimary() {
    ConfigurableListableBeanFactory beanFactory =
        ((AbstractApplicationContext) context).getBeanFactory();
    try {
      BeanDefinition transactionManager =
          beanFactory.getBeanDefinition(GnsSyncConstants.PRIMARY_TRANSACTION_MANAGER);
      transactionManager.setPrimary(true);
    } catch (NoSuchBeanDefinitionException ex) {
      log.error("Transaction manager bean not found. Cannot make it primary", ex);
    }
  }

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
