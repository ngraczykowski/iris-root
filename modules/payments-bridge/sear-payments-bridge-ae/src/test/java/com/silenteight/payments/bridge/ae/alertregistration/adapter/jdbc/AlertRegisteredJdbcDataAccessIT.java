package com.silenteight.payments.bridge.ae.alertregistration.adapter.jdbc;

import com.silenteight.payments.bridge.testing.JdbcTestConfiguration;
import com.silenteight.sep.base.testing.BaseJdbcTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import java.util.Collections;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Sql
@ContextConfiguration(classes = {
    JdbcTestConfiguration.class,
    AlertRegisteredJdbcDataAccess.class
})
class AlertRegisteredJdbcDataAccessIT extends BaseJdbcTest {

  @Autowired
  private AlertRegisteredJdbcDataAccess dataAccess;

  @Test
  void shouldSelectAllRegisteredAlerts() {
    var response = dataAccess.findRegistered(
        Collections.singletonList(UUID.fromString("f07f327c-58c2-e2e5-b02d-b2bdeee79adc")));
    assertThat(response.get(0).getMatches().size()).isEqualTo(2);
  }
}
