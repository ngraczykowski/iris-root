package com.silenteight.payments.bridge.ae.alertregistration.adapter.jdbc;

import com.silenteight.sep.base.testing.BaseJdbcTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import java.util.Collections;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Sql
@Import(AlertRegisteredJdbcDataAccess.class)
class AlertRegisteredJdbcDataAccessIT extends BaseJdbcTest {

  @Autowired
  private AlertRegisteredJdbcDataAccess dataAccess;

  @Test
  void shouldSelectAllRegisteredAlerts() {
    var response = dataAccess.findRegistered(
        Collections.singletonList("1"));
    assertThat(response.get(0).getMatches().size()).isEqualTo(2);
  }
}
