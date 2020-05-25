package com.silenteight.serp.common.testing;

import com.silenteight.serp.common.database.DataSourceAutoConfiguration;
import com.silenteight.serp.common.testing.containers.PostgresContainer.PostgresTestInitializer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;

import javax.sql.DataSource;

@JdbcTest
@ContextConfiguration(initializers = { PostgresTestInitializer.class })
@ImportAutoConfiguration(DataSourceAutoConfiguration.class)
@SuppressWarnings("squid:S1694")
public abstract class BaseJdbcTest {

  @Autowired
  protected DataSource dataSource;

  @Autowired
  protected JdbcTemplate jdbcTemplate;
}
