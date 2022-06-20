/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.decisiongroups;

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.SyncTestInitializer;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.config.SyncDataSourcesConfiguration;
import com.silenteight.sep.base.testing.BaseJdbcTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.transaction.annotation.Propagation.NEVER;

@ContextConfiguration(initializers = SyncTestInitializer.class, classes = {
    ExternalJdbcTemplateConfiguration.class,
    JdbcDecisionGroupsReaderConfiguration.class,
    SyncDataSourcesConfiguration.class
})
@SqlConfig(dataSource = "externalDataSource", transactionManager = "externalTransactionManager")
@TestPropertySource(properties = "spring.external.datasource.hikari.connection-init-sql=SELECT 1")
@Transactional(propagation = NEVER, transactionManager = "externalTransactionManager")
class JdbcDecisionGroupsReaderIT extends BaseJdbcTest {

  @Autowired
  private JdbcDecisionGroupsReader reader;

  @Test
  @Sql(scripts = "testGroups.sql")
  void readsAllGroups() {
    assertThat(reader.readAll()).contains("group 1", "group 2");
  }

  @Test
  void overrideQuery() {
    reader.setQuery("SELECT 'unit'");
    assertThat(reader.readAll()).contains("unit");
  }

  @Test
  void failsWhenNoRelationAndNoQueryGiven() {
    reader.setRelationName("");
    assertThatThrownBy(() -> reader.readAll()).isInstanceOf(IllegalStateException.class);
  }
}
