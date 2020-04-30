package com.silenteight.auditing.bs;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.UUID;

import static com.silenteight.auditing.bs.AuditTableColumns.*;

@RequiredArgsConstructor
public class AuditingFinder {

  private static final String SELECT_LOGS_QUERY = "SELECT * FROM audit"
      + " WHERE " + TIMESTAMP + " >= :from AND " + TIMESTAMP + " <= :to";

  @NonNull
  private final NamedParameterJdbcTemplate jdbcTemplate;

  public Collection<AuditDataDto> find(@NonNull OffsetDateTime from, @NonNull OffsetDateTime to) {
    MapSqlParameterSource paramSource = new MapSqlParameterSource()
        .addValue("from", from)
        .addValue("to", to);

    return jdbcTemplate.query(SELECT_LOGS_QUERY, paramSource, (rs, rowNum) -> createAuditData(rs));
  }

  private static AuditDataDto createAuditData(ResultSet resultSet) throws SQLException {
    return AuditDataDto
        .builder()
        .eventId(resultSet.getObject(EVENT_ID, UUID.class))
        .correlationId(resultSet.getObject(CORRELATION_ID, UUID.class))
        .timestamp(resultSet.getTimestamp(TIMESTAMP))
        .type(resultSet.getString(TYPE))
        .principal(resultSet.getString(PRINCIPAL))
        .entityId(resultSet.getString(ENTITY_ID))
        .entityClass(resultSet.getString(ENTITY_CLASS))
        .entityAction(resultSet.getString(ENTITY_ACTION))
        .details(resultSet.getString(DETAILS))
        .build();
  }
}
