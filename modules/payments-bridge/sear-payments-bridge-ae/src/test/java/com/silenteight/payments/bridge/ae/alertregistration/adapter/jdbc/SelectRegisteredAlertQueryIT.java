package com.silenteight.payments.bridge.ae.alertregistration.adapter.jdbc;

import com.silenteight.sep.base.testing.BaseJdbcTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Sql
@Import({
    SelectRegisteredAlertQuery.class
})
class SelectRegisteredAlertQueryIT extends BaseJdbcTest {

  @Autowired
  private SelectRegisteredAlertQuery selectRegisteredAlertQuery;

  @Test
  public void shouldSelectAlert() {
    var systemId = selectRegisteredAlertQuery.execute("alerts/1");
    assertThat(systemId).isEqualTo("systemId");
  }
}
