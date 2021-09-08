package com.silenteight.adjudication.engine.analysis.agentexchange.jdbc;

import com.silenteight.sep.base.testing.BaseJdbcTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.*;

@Sql
@ContextConfiguration(classes = {
    JdbcAgentExchangeDataAccess.class,
    DeleteEmptyAgentExchange.class,
    DeleteAgentExchangeMatchFeatureQuery.class
})
class JdbcAgentExchangeDataAccessIT extends BaseJdbcTest {

  @Autowired
  private JdbcAgentExchangeDataAccess dataAccess;

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Test
  void shouldDeleteAgentExchangeMatchFeature() {
    dataAccess.removeAgentExchange();
    assertThat(jdbcTemplate.queryForObject(
        "SELECT count(*) FROM ae_agent_exchange_match_feature",
        Integer.class)).isEqualTo(2);
  }

  @Test
  void shouldDeleteEmptyAgentExchange() {
    dataAccess.removeAgentExchange();
    assertThat(jdbcTemplate.queryForObject(
        "SELECT count(*) FROM ae_agent_exchange",
        Integer.class)).isEqualTo(1);
  }

  @Test
  void shouldDeleteEmptyAgentExchangeFeature() {
    dataAccess.removeAgentExchange();
    assertThat(jdbcTemplate.queryForObject(
        "SELECT count(*) FROM ae_agent_exchange_feature",
        Integer.class)).isEqualTo(1);
  }
}
