package com.silenteight.sens.webapp.common.testing;

import com.silenteight.sens.webapp.common.database.DataSourceAutoConfiguration;
import com.silenteight.sens.webapp.common.database.TransactionManagerAutoConfiguration;
import com.silenteight.sens.webapp.common.testing.containers.PostgresContainer.PostgresTestInitializer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;

import javax.sql.DataSource;

@JdbcTest
@ContextConfiguration(initializers = { PostgresTestInitializer.class })
@ImportAutoConfiguration({
    DataSourceAutoConfiguration.class,
    TransactionManagerAutoConfiguration.class
})
@SuppressWarnings("squid:S1694")
public abstract class BaseJdbcTest {

  @Autowired
  protected DataSource dataSource;

  @Autowired
  protected JdbcTemplate jdbcTemplate;
}
