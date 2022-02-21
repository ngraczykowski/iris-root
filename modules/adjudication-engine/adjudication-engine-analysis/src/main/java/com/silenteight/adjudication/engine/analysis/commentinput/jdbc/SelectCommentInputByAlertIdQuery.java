package com.silenteight.adjudication.engine.analysis.commentinput.jdbc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.base.common.support.jackson.JsonConversionHelper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import static org.apache.commons.lang3.exception.ExceptionUtils.rethrow;

@Slf4j
class SelectCommentInputByAlertIdQuery {

  private static final String COMMENT_INPUT_VALUE =
      "SELECT value"
          + " FROM ae_alert_comment_input a"
          + " WHERE a.alert_id = ?";

  private final JdbcTemplate jdbcTemplate;
  private final ObjectMapper objectMapper;
  private final MapType commentInputType;

  SelectCommentInputByAlertIdQuery(JdbcTemplate jdbcTemplate, ObjectMapper objectMapper) {
    this.jdbcTemplate = jdbcTemplate;
    this.objectMapper = objectMapper;
    commentInputType = JsonConversionHelper.INSTANCE
        .objectMapper()
        .getTypeFactory()
        .constructMapType(LinkedHashMap.class, String.class, Object.class);
  }

  Optional<Map<String, Object>> execute(long alertId) {
    var rows = jdbcTemplate.query(
        COMMENT_INPUT_VALUE, new SqlCommentInputExtractor(objectMapper), alertId);
    return rows.stream().findFirst();
  }

  @RequiredArgsConstructor
  static class SqlCommentInputExtractor implements RowMapper<Map<String, Object>> {

    private final ObjectMapper objectMapper;
    private static final MapType MAP_TYPE = JsonConversionHelper.INSTANCE
        .objectMapper()
        .getTypeFactory()
        .constructMapType(LinkedHashMap.class, String.class, Object.class);

    @Override
    public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
      ObjectMapper mapper = new ObjectMapper();
      try {
        var value = rs.getString("value");
        Map<String, Object> commentInput = objectMapper.readValue(value, MAP_TYPE);
        log.trace("Found comment input: {}", commentInput);
        return commentInput;
      } catch (JsonProcessingException e) {
        log.error("There is an exception during mapping comment: {}", e.getMessage());
        return rethrow(e);
      }
    }
  }
}
