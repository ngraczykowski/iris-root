package com.silenteight.adjudication.engine.features.matchfeaturevalue.jdbc;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.features.matchfeaturevalue.dto.MatchFeatureValue;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Struct;
import com.google.protobuf.util.JsonFormat;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.SqlUpdate;

import java.sql.Types;
import java.util.stream.StreamSupport;

@Slf4j
class InsertMatchFeatureValueBatchQuery {

  private static final JsonFormat.Printer JSON_PRINTER = JsonFormat.printer();

  private final SqlUpdate sql;

  InsertMatchFeatureValueBatchQuery(JdbcTemplate jdbcTemplate) {
    sql = new SqlUpdate();

    sql.setJdbcTemplate(jdbcTemplate);
    sql.setSql("INSERT INTO ae_match_feature_value(match_id, "
        + "agent_config_feature_id, "
        + "created_at, "
        + "value, "
        + "reason)\n"
        + "VALUES (?, ?, now(), ?, ? ::jsonb)\n"
        + "ON CONFLICT DO NOTHING");
    sql.declareParameter(new SqlParameter("match_id", Types.BIGINT));
    sql.declareParameter(new SqlParameter("agent_config_feature_id", Types.BIGINT));
    sql.declareParameter(new SqlParameter("value", Types.VARCHAR));
    sql.declareParameter(new SqlParameter("reason", Types.VARCHAR));

    sql.compile();
  }

  @SuppressWarnings("FeatureEnvy")
  int execute(Iterable<MatchFeatureValue> featureValues) {
    return StreamSupport
        .stream(featureValues.spliterator(), false)
        .mapToInt(v -> sql.update(
            v.getMatchId(),
            v.getAgentConfigFeatureId(),
            v.getValue(),
            toJson(v.getReason())))
        .sum();
  }

  private static String toJson(Struct reason) {
    try {
      return JSON_PRINTER.print(reason);
    } catch (InvalidProtocolBufferException e) {
      log.warn("Failed to convert reason to JSON", e);
      return "{\"error\": "
          + "\"Failed to convert reason to JSON, please check the log files for details\""
          + "}";
    }
  }
}
