package com.silenteight.payments.bridge.ae.alertregistration.adapter.jdbc;

import com.silenteight.payments.bridge.testing.JdbcTestConfiguration;
import com.silenteight.sep.base.testing.BaseJdbcTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Sql
@ContextConfiguration(classes = {
    JdbcTestConfiguration.class,
    RegisterMatchJdbcDataAccess.class,
    RegisterMatchJdbcConfiguration.class
})
class RegisterMatchJdbcDataAccessIT extends BaseJdbcTest {

  @Autowired
  private RegisterMatchJdbcDataAccess dataAccess;
  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Test
  void shouldInsertAllMatches() {
    dataAccess.save(
        UUID.fromString("f07f327c-58c2-e2e5-b02d-b2bdeee79adc"),
        List.of("alerts/1/matches/1", "alerts/1/matches/2"));
    assertThat(jdbcTemplate.queryForObject(
        "SELECT count(*) FROM pb_registered_match",
        Integer.class)).isEqualTo(2);
  }
}
