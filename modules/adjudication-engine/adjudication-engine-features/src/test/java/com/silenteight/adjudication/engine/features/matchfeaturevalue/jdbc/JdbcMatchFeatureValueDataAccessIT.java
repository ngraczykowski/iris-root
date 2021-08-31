package com.silenteight.adjudication.engine.features.matchfeaturevalue.jdbc;

import com.silenteight.adjudication.engine.features.matchfeaturevalue.dto.MatchFeatureValue;
import com.silenteight.adjudication.engine.testing.JdbcTestConfiguration;
import com.silenteight.sep.base.testing.BaseJdbcTest;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Struct;
import com.google.protobuf.util.JsonFormat;
import com.google.protobuf.util.Values;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@ContextConfiguration(classes = {
    JdbcTestConfiguration.class,
    JdbcMatchFeatureValueDataAccess.class,
})
@Sql
class JdbcMatchFeatureValueDataAccessIT extends BaseJdbcTest {

  private static final String MATCH = "MATCH";
  private static final String NO_MATCH = "NO_MATCH";
  protected static final String REASON_FIELD = "testReason";

  @Autowired
  private JdbcMatchFeatureValueDataAccess dataAccess;

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Test
  void savesMatchFeatureValues() {
    var savedCount = dataAccess.saveAll(List.of(
        matchFeatureValue(1, 1, MATCH),
        matchFeatureValue(1, 2, NO_MATCH),
        matchFeatureValue(2, 1, MATCH),
        matchFeatureValue(2, 2, NO_MATCH),
        matchFeatureValue(3, 1, MATCH),
        matchFeatureValue(3, 2, NO_MATCH),
        matchFeatureValue(4, 1, NO_MATCH),
        matchFeatureValue(4, 2, MATCH)
    ));

    var dbCount =
        jdbcTemplate.queryForObject("SELECT count(*) FROM ae_match_feature_value", Long.class);

    assertThat(dbCount).isEqualTo(savedCount);
  }

  @Test
  void reasonDeserializesCorrectly() throws InvalidProtocolBufferException {
    dataAccess.saveAll(List.of(matchFeatureValue(1, 1, MATCH)));

    var reasonJson = jdbcTemplate.queryForObject(
        "SELECT reason\n"
            + "FROM ae_match_feature_value\n"
            + "WHERE agent_config_feature_id = 1\n"
            + "  AND match_id = 1",
        String.class);

    var reasonBuilder = Struct.newBuilder();
    JsonFormat.parser().merge(reasonJson, reasonBuilder);
    var reason = reasonBuilder.build();

    assertThat(reason.getFieldsMap())
        .hasEntrySatisfying(
            REASON_FIELD, r -> assertThat(r.getStringValue()).startsWith("the value was "));
  }

  private static MatchFeatureValue matchFeatureValue(
      long matchId, long agentConfigFeatureId, String value) {

    return MatchFeatureValue.builder()
        .matchId(matchId)
        .agentConfigFeatureId(agentConfigFeatureId)
        .value(value)
        .reason(Struct
            .newBuilder()
            .putFields(REASON_FIELD, Values.of("the value was " + value))
            .build())
        .build();
  }
}
