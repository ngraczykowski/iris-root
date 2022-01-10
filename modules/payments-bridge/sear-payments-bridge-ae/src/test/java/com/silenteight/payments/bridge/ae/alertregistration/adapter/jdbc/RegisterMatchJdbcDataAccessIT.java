package com.silenteight.payments.bridge.ae.alertregistration.adapter.jdbc;

import com.silenteight.payments.bridge.ae.alertregistration.domain.SaveRegisteredAlertRequest;
import com.silenteight.payments.bridge.ae.alertregistration.domain.SaveRegisteredMatchRequest;
import com.silenteight.sep.base.testing.BaseJdbcTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Sql
@Import({
    RegisteredAlertJdbcDataAccess.class,
    InsertRegisteredAlertQuery.class,
    SelectRegisteredAlertQuery.class,
    InsertMatchQuery.class,
    DeleteRegisteredAlertQuery.class
})
class RegisterMatchJdbcDataAccessIT extends BaseJdbcTest {

  @Autowired
  private RegisteredAlertJdbcDataAccess dataAccess;

  @Test
  void shouldInsertAllMatches() {
    var request = SaveRegisteredAlertRequest
        .builder()
        .alertId(UUID.fromString("f07f327c-58c2-e2e5-b02d-b2bdeee79adc"))
        .fkcoSystemId("systemId")
        .fkcoMessageId("messageId")
        .alertName("alerts/1")
        .matches(List.of(SaveRegisteredMatchRequest
            .builder()
            .matchName("alerts/1/matches/1")
            .matchId("matchId")
            .build(), SaveRegisteredMatchRequest
            .builder()
            .matchName("alerts/1/matches/2")
            .matchId("matchId")
            .build()))
        .build();
    dataAccess.save(List.of(request));

    assertThat(jdbcTemplate.queryForObject(
        "SELECT count(*) FROM pb_registered_alert",
        Integer.class)).isEqualTo(1);
    assertThat(jdbcTemplate.queryForObject(
        "SELECT count(*) FROM pb_registered_match",
        Integer.class)).isEqualTo(2);
  }
}
