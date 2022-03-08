package com.silenteight.scb.ingest.adapter.incomming.common;

import com.silenteight.sep.base.testing.containers.PostgresContainer;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

public class SyncTestInitializer
    implements ApplicationContextInitializer<ConfigurableApplicationContext> {

  @Override
  public void initialize(ConfigurableApplicationContext applicationContext) {
    TestPropertyValues propertyValues = TestPropertyValues.of(
        "spring.liquibase.change-log=classpath:db/changelog/db.changelog-master-test.xml",
        "spring.external.datasource.url=" + PostgresContainer.getJdbcUrl(),
        "spring.external.datasource.username=" + PostgresContainer.getUsername(),
        "spring.external.datasource.password=" + PostgresContainer.getPassword(),
        "spring.external.datasource.driver-class-name=" + PostgresContainer.getDriverClassName(),
        "spring.external.datasource.hikari.schema=gns",
        "spring.external.datasource.hikari-on-demand.schema=gns",
        "serp.gns.db.connection-init-query=SELECT 1"
    );

    propertyValues.applyTo(applicationContext.getEnvironment());
  }
}
