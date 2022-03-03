package com.silenteight.customerbridge.cbs.alertrecord;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.customerbridge.common.alertrecord.AlertRecord;
import com.silenteight.customerbridge.common.alertrecord.AlertRecordMapper;

import com.google.common.collect.Lists;
import org.springframework.jdbc.core.ArgumentPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.validation.ValidationException;

import static com.silenteight.customerbridge.cbs.alertrecord.AlertsReaderQueryTemplates.RECORDS_QUERY;
import static com.silenteight.customerbridge.cbs.batch.QueryStatementHelper.prepareQuery;
import static java.util.stream.Collectors.toList;
import static org.springframework.transaction.annotation.Isolation.SERIALIZABLE;

@Slf4j
@RequiredArgsConstructor
class DatabaseAlertRecordReader implements AlertRecordReader {

  private final JdbcAlertRecordMapper mapper = new JdbcAlertRecordMapper();

  private final JdbcTemplate jdbcTemplate;

  @Override
  @Transactional(
      transactionManager = "externalTransactionManager",
      isolation = SERIALIZABLE,
      readOnly = true)
  public List<AlertRecord> read(String dbRelationName, Collection<String> systemIds) {
    if (systemIds.isEmpty()) {
      return List.of();
    }

    return Lists.partition(new ArrayList<>(systemIds), getMaxPageSize()).stream()
        .map(ids -> getAlertRecords(dbRelationName, ids))
        .flatMap(Collection::stream)
        .filter(Objects::nonNull)
        .collect(toList());
  }

  @Nonnull
  private List<AlertRecord> getAlertRecords(
      String dbRelationName, Collection<String> systemIds) {
    var query = prepareQuery(RECORDS_QUERY, dbRelationName, systemIds);
    return jdbcTemplate.query(query, getArgumentSetter(systemIds), mapper);
  }

  @Nonnull
  private static ArgumentPreparedStatementSetter getArgumentSetter(Collection<String> systemIds) {
    return new ArgumentPreparedStatementSetter(systemIds.toArray());
  }

  @RequiredArgsConstructor
  private static class JdbcAlertRecordMapper implements RowMapper<AlertRecord> {

    @Nullable
    @Override
    public AlertRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
      try {
        return AlertRecordMapper.mapResultSet(rs);
      } catch (ValidationException e) {
        String systemId = rs.getString("system_id");
        log.error("Alert record validation failed: systemId={}", systemId, e);
        return null;
      }
    }
  }
}
