package com.silenteight.adjudication.engine.trace.infrastructure.jdbc;

import com.silenteight.adjudication.engine.trace.domain.Trace;
import com.silenteight.adjudication.engine.trace.domain.TraceRepository;
import com.silenteight.sep.base.common.support.jackson.JsonConversionHelper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Timestamp;
import java.sql.Types;
import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.Arrays;

class TraceJdbcRepository implements TraceRepository {

  private final JdbcTemplate jdbcTemplate;
  private static final ObjectMapper MAPPER = JsonConversionHelper.INSTANCE.objectMapper();

  @Language("PostgreSQL")
  private static final String SQL = "INSERT INTO \n"
      + "ae_event_journal"
      + "(event_id, event_type, payload, recommendation_hash, occurred_on, created_at) \n"
      + "VALUES (?, ?, ?, ?, ?, ?)";

  TraceJdbcRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public void save(Trace trace) {
    var createdAt = Timestamp.valueOf(OffsetDateTime.now(Clock.systemUTC()).toLocalDateTime());
    var occurredOn = Timestamp.valueOf(trace.getOcurrenceOn().toLocalDateTime());
    jdbcTemplate.update(
        SQL,
        Arrays.asList(trace.getEventId(), trace.getEventType(), trace.getPayload(),
            trace.getPayloadHash(), occurredOn, createdAt).toArray(),
        new int[] {
            Types.OTHER, Types.VARCHAR, Types.OTHER, Types.VARCHAR, Types.TIMESTAMP,
            Types.TIMESTAMP });
  }
}
