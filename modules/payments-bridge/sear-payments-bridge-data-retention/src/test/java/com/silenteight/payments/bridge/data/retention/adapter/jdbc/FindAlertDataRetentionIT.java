package com.silenteight.payments.bridge.data.retention.adapter.jdbc;

import com.silenteight.payments.bridge.data.retention.model.DataType;
import com.silenteight.payments.bridge.testing.JdbcTestConfiguration;
import com.silenteight.sep.base.testing.BaseJdbcTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Sql
@ContextConfiguration(classes = {
    JdbcTestConfiguration.class,
    FindAlertDataRetention.class,
})
class FindAlertDataRetentionIT extends BaseJdbcTest {

  @Autowired private FindAlertDataRetention findAlertDataRetention;

  @Test
  public void shouldFoundOneAlertToExpirePersonalInformation() {
    var before = OffsetDateTime.now().minusDays(1);
    var result = findAlertDataRetention.findAlertTimeBefore(
        before, DataType.PERSONAL_INFORMATION);
    assertEquals(1, result.size());
    assertEquals("alerts/3", result.get(0));
  }

  @Test
  public void shouldFoundOneAlertToExpireAlertData() {
    var before = OffsetDateTime.now().minusDays(1);
    var result = findAlertDataRetention.findAlertTimeBefore(
        before, DataType.ALERT_DATA);
    assertEquals(1, result.size());
    assertEquals("alerts/2", result.get(0));
  }

}
