package com.silenteight.payments.bridge.data.retention.adapter.jdbc;

import com.silenteight.payments.bridge.data.retention.model.AlertDataRetention;
import com.silenteight.sep.base.testing.BaseJdbcTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import java.time.OffsetDateTime;
import java.util.List;

@Sql
@Import(InsertAlertDataRetention.class)
class InsertAlertDataRetentionIT extends BaseJdbcTest {

  @Autowired
  private InsertAlertDataRetention insertAlertDataRetention;

  @Test
  void shouldInsertAlertDataRetention() {
    insertAlertDataRetention.update(
        List.of(new AlertDataRetention("alerts/2", OffsetDateTime.now())));
  }

  @Test
  void shouldUpdateSameAlertDataRetention() {
    insertAlertDataRetention.update(
        List.of(new AlertDataRetention("alerts/3", OffsetDateTime.now())));

    insertAlertDataRetention.update(
        List.of(new AlertDataRetention("alerts/3", OffsetDateTime.now())));
  }
}
