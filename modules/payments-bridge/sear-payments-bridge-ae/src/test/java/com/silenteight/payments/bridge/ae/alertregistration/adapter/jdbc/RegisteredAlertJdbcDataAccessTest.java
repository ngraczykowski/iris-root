package com.silenteight.payments.bridge.ae.alertregistration.adapter.jdbc;

import com.silenteight.sep.base.testing.BaseJdbcTest;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

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
  @Disabled
  void shouldDeleteAlerts() {

    var alerts = getListOfAlerts(10, 20, 30, 40, 50);

    List<String> deletedAlertsMessageId = dataAccess.delete(alerts);

    assertEquals(10, jdbcTemplate.queryForObject(
        "SELECT count(*) FROM pb_registered_alert", Integer.class));
    assertEquals(12, jdbcTemplate.queryForObject(
        "SELECT count(*) FROM pb_registered_match", Integer.class));
  }
}
