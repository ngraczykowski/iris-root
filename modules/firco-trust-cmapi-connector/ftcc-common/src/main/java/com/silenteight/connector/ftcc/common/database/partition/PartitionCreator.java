package com.silenteight.connector.ftcc.common.database.partition;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static java.lang.String.format;
import static java.time.format.DateTimeFormatter.ofPattern;

@Slf4j
@RequiredArgsConstructor
public class PartitionCreator {

  private static final DateTimeFormatter RANGE_FORMATTER = ofPattern("yyyy-MM'-01'");
  private static final String CREATE_PARTITION_QUERY =
      "CREATE TABLE IF NOT EXISTS %s PARTITION OF %s FOR VALUES FROM ('%s') TO ('%s')";

  @NonNull
  private final NamedParameterJdbcTemplate jdbcTemplate;

  @Transactional
  public void createPartition(@NonNull String tableName, @NonNull OffsetDateTime startTime) {
    String query = buildQuery(tableName, startTime);
    log.debug("Creating partition with query {}", query);
    jdbcTemplate.execute(query, Map.of(), PreparedStatement::execute);
  }

  private static String buildQuery(String tableName, OffsetDateTime startTime) {
    String partitionName = buildPartitionName(tableName, startTime);

    OffsetDateTime endTime = startTime.plusMonths(1);
    String rangeFrom = RANGE_FORMATTER.format(startTime);
    String rangeTo = RANGE_FORMATTER.format(endTime);

    return format(CREATE_PARTITION_QUERY, partitionName, tableName, rangeFrom, rangeTo);
  }

  private static String buildPartitionName(String tableName, OffsetDateTime startTime) {
    DateTimeFormatter partitionNameFormatter = ofPattern("'" + tableName + "_'yyyy_MM");
    return partitionNameFormatter.format(startTime);
  }
}
