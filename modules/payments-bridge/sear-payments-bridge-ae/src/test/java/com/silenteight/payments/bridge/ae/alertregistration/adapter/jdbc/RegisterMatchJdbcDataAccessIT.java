package com.silenteight.payments.bridge.ae.alertregistration.adapter.jdbc;

import com.silenteight.payments.bridge.ae.alertregistration.domain.SaveRegisteredAlertRequest;
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
    RegisteredAlertJdbcDataAccess.class,
    InsertRegisteredAlertQuery.class,
    SelectRegisteredAlertQuery.class,
    InsertMatchQuery.class
})
class RegisterMatchJdbcDataAccessIT extends BaseJdbcTest {

  @Autowired
  private RegisteredAlertJdbcDataAccess dataAccess;
  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Test
  void shouldInsertAllMatches() {
    var request = SaveRegisteredAlertRequest
        .builder()
        .alertId(UUID.fromString("f07f327c-58c2-e2e5-b02d-b2bdeee79adc"))
        .alertName("alerts/1")
        .matchNames(List.of("alerts/1/matches/1", "alerts/1/matches/2"))
        .build();
    dataAccess.save(request);

    assertThat(jdbcTemplate.queryForObject(
        "SELECT count(*) FROM pb_registered_alert",
        Integer.class)).isEqualTo(1);
    assertThat(jdbcTemplate.queryForObject(
        "SELECT count(*) FROM pb_registered_match",
        Integer.class)).isEqualTo(2);
  }
}
