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
import static java.util.Collections.emptyList;

@RequiredArgsConstructor
public class AuditingFinder {

  private static final String SORTING_ORDER = "ASC";

  private static final String FIND_LOGS_QUERY_SELECT_PART = "SELECT * FROM audit";
  private static final String FIND_LOGS_QUERY_WHERE_PART =
      " WHERE " + TIMESTAMP + " >= :from AND " + TIMESTAMP + " <= :to";
  private static final String FIND_LOGS_BY_TYPES_QUERY_WHERE_PART =
      " WHERE " + TIMESTAMP + " >= :from AND " + TIMESTAMP + " <= :to AND "
          + TYPE + " IN (:types)";
  private static final String FIND_LOGS_QUERY_ORDER_BY_PART =
      " ORDER BY " + TIMESTAMP + " " + SORTING_ORDER;

  private static final String FIND_LOGS_QUERY = FIND_LOGS_QUERY_SELECT_PART
      + FIND_LOGS_QUERY_WHERE_PART
      + FIND_LOGS_QUERY_ORDER_BY_PART;

  private static final String FIND_LOGS_BY_TYPES_QUERY = FIND_LOGS_QUERY_SELECT_PART
      + FIND_LOGS_BY_TYPES_QUERY_WHERE_PART
      + FIND_LOGS_QUERY_ORDER_BY_PART;

  @NonNull
  private final NamedParameterJdbcTemplate jdbcTemplate;

  public Collection<AuditDataDto> find(@NonNull OffsetDateTime from, @NonNull OffsetDateTime to) {
    return find(from, to, emptyList());
  }

  public Collection<AuditDataDto> find(
      @NonNull OffsetDateTime from,
      @NonNull OffsetDateTime to,
      @NonNull Collection<String> types) {

    MapSqlParameterSource paramSource = new MapSqlParameterSource()
        .addValue("from", from)
        .addValue("to", to);

    if (!types.isEmpty())
      paramSource.addValue("types", types);

    return jdbcTemplate.query(getQuery(types), paramSource, (rs, rowNum) -> createAuditData(rs));
  }

  private static String getQuery(Collection<String> types) {
    return types.isEmpty() ? FIND_LOGS_QUERY : FIND_LOGS_BY_TYPES_QUERY;
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
