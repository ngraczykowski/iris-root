package com.silenteight.customerbridge.cbs.alertrecord;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.customerbridge.common.alertrecord.DecisionRecord;

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

import static com.silenteight.customerbridge.cbs.alertrecord.AlertsReaderQueryTemplates.DECISIONS_QUERY;
import static com.silenteight.customerbridge.cbs.batch.QueryStatementHelper.prepareQuery;
import static java.util.stream.Collectors.toList;
import static org.springframework.transaction.annotation.Isolation.SERIALIZABLE;

@Slf4j
class DatabaseDecisionRecordReader implements DecisionRecordReader {

  private final JdbcTemplate jdbcTemplate;
  private final JdbcDecisionRecordMapper mapper;

  DatabaseDecisionRecordReader(
      JdbcTemplate jdbcTemplate, DecisionRecordRowMapper rowMapper) {

    this.jdbcTemplate = jdbcTemplate;
    this.mapper = new JdbcDecisionRecordMapper(rowMapper);
  }

  @Override
  @Transactional(
      transactionManager = "externalTransactionManager",
      isolation = SERIALIZABLE,
      readOnly = true)
  public List<DecisionRecord> read(String dbRelationName, Collection<String> systemIds) {
    if (systemIds.isEmpty()) {
      return List.of();
    }

    return Lists.partition(new ArrayList<>(systemIds), getMaxPageSize()).stream()
        .map(ids -> getDecisionRecords(dbRelationName, ids))
        .flatMap(Collection::stream)
        .filter(Objects::nonNull)
        .collect(toList());
  }

  @Nonnull
  private List<DecisionRecord> getDecisionRecords(
      String dbRelationName, Collection<String> systemIds) {
    var query = prepareQuery(DECISIONS_QUERY, dbRelationName, systemIds);
    return jdbcTemplate.query(query, getArgumentSetter(systemIds), mapper);
  }

  @Nonnull
  private static ArgumentPreparedStatementSetter getArgumentSetter(Collection<String> systemIds) {
    return new ArgumentPreparedStatementSetter(systemIds.toArray());
  }

  @RequiredArgsConstructor
  private static class JdbcDecisionRecordMapper implements RowMapper<DecisionRecord> {

    private final DecisionRecordRowMapper decisionRecordRowMapper;

    @Nullable
    @Override
    public DecisionRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
      try {
        return decisionRecordRowMapper.mapResultSet(rs);
      } catch (ValidationException e) {
        String systemId = rs.getString("system_id");
        log.error("Decision record validation failed: systemId={}", systemId, e);
        return null;
      }
    }
  }
}
