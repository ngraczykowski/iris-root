package com.silenteight.fab.dataprep.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
class AlertRepositoryExtImpl implements AlertRepositoryExt {

  private static final DateTimeFormatter NAME_FORMATTER =
      DateTimeFormatter.ofPattern("'dataprep_alert_'yyyy_MM");
  private static final DateTimeFormatter RANGE_FORMATTER =
      DateTimeFormatter.ofPattern("yyyy-MM'-01'");

  private final NamedParameterJdbcTemplate jdbcTemplate;

  private static final String CREATE_PARTITION_QUERY =
      "CREATE TABLE IF NOT EXISTS %s PARTITION OF dataprep_alert FOR VALUES FROM ('%s') TO ('%s')";

  @Override
  @Transactional
  public void createPartition(OffsetDateTime partitionMonth) {
    String partitionName = NAME_FORMATTER.format(partitionMonth);
    String rangeFrom = RANGE_FORMATTER.format(partitionMonth);
    partitionMonth = partitionMonth.plusMonths(1);
    String rangeTo = RANGE_FORMATTER.format(partitionMonth);
    log.debug("Create partition for range {} - {}", rangeFrom, rangeTo);
    jdbcTemplate.execute(
        getQuery(partitionName, rangeFrom, rangeTo), Map.of(), PreparedStatement::execute);
  }

  private static String getQuery(
      String partitionName,
      String rangeFrom,
      String rangeTo) {
    return String.format(CREATE_PARTITION_QUERY, partitionName, rangeFrom, rangeTo);
  }
}
