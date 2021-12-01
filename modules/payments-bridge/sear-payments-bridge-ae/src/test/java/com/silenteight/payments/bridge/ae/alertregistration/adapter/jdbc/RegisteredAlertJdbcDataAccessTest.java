package com.silenteight.payments.bridge.ae.alertregistration.adapter.jdbc;

import com.silenteight.sep.base.testing.BaseJdbcTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.UUID;

import static com.silenteight.payments.bridge.ae.alertregistration.AlertFixture.getListOfAlerts;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Sql
@Import({
    RegisteredAlertJdbcDataAccess.class,
    InsertRegisteredAlertQuery.class,
    SelectRegisteredAlertQuery.class,
    InsertMatchQuery.class,
    DeleteRegisteredAlertQuery.class
})
class RegisteredAlertJdbcDataAccessTest extends BaseJdbcTest {

  @Autowired
  private RegisteredAlertJdbcDataAccess dataAccess;

  @Test
  void shouldDeleteAlerts() {

    var alerts = getListOfAlerts(1, 2, 3, 4, 5);

    List<UUID> deletedAlertsMessageId = dataAccess.delete(alerts);

    assertEquals(5, deletedAlertsMessageId.size());
    assertEquals(5, jdbcTemplate.queryForObject(
        "SELECT count(*) FROM pb_registered_alert", Integer.class));
    assertEquals(6, jdbcTemplate.queryForObject(
        "SELECT count(*) FROM pb_registered_match", Integer.class));
    assertEquals(1, deletedAlertsMessageId.stream()
        .filter(u -> u.equals(UUID.fromString("f07f327c-58c2-e2e5-b02d-b2bdeee79ad1")))
        .count());
  }
}
