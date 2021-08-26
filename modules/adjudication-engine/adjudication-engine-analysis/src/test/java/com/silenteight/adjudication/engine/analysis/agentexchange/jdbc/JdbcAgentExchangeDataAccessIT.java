package com.silenteight.adjudication.engine.analysis.agentexchange.jdbc;

import com.silenteight.sep.base.testing.BaseJdbcTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@Sql
@ContextConfiguration(classes = {
    JdbcAgentExchangeDataAccess.class,
    DeleteEmptyAgentExchange.class,
    JdbcAgentExchangeConfiguration.class
})
class JdbcAgentExchangeDataAccessIT extends BaseJdbcTest {

  @Autowired
  private JdbcAgentExchangeDataAccess dataAccess;

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Test
  void shouldDeleteAgentExchangeMatchFeature() {
    dataAccess.removeAgentExchange(
        UUID.fromString("980e1f4c-6c5b-45d2-8516-0998776a39c8"), 1,
        List.of("features/dob", "features/name"));
    assertThat(jdbcTemplate.queryForObject(
        "SELECT count(*) FROM ae_agent_exchange_match_feature",
        Integer.class)).isEqualTo(2);
  }

  @Test
  void shouldDeleteEmptyAgentExchange() {
    dataAccess.removeAgentExchange(
        UUID.fromString("980e1f4c-6c5b-45d2-8516-0998776a39c8"), 1,
        List.of("features/dob", "features/name"));
    assertThat(jdbcTemplate.queryForObject(
        "SELECT count(*) FROM ae_agent_exchange",
        Integer.class)).isEqualTo(1);
  }

  @Test
  void shouldDeleteEmptyAgentExchangeFeature() {
    dataAccess.removeAgentExchange(
        UUID.fromString("980e1f4c-6c5b-45d2-8516-0998776a39c8"), 1,
        List.of("features/dob", "features/name"));
    assertThat(jdbcTemplate.queryForObject(
        "SELECT count(*) FROM ae_agent_exchange_feature",
        Integer.class)).isEqualTo(1);
  }
}
