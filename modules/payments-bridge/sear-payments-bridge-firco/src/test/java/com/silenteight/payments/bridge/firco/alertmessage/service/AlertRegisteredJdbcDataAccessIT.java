package com.silenteight.payments.bridge.firco.alertmessage.service;

import com.silenteight.payments.bridge.common.model.FindRegisteredAlertRequest;
import com.silenteight.payments.bridge.testing.JdbcTestConfiguration;
import com.silenteight.sep.base.testing.BaseJdbcTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import java.util.Collections;

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
        Collections.singletonList(new FindRegisteredAlertRequest("systemId", "messageId")));
    assertThat(response.get(0).getMatches().size()).isEqualTo(2);
  }
}
