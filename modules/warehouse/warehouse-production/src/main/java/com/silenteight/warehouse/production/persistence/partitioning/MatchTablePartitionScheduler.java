package com.silenteight.warehouse.production.persistence.partitioning;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.jetbrains.annotations.NotNull;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.transaction.Transactional;


@RequiredArgsConstructor
@Slf4j
public class MatchTablePartitionScheduler {

  private final NamedParameterJdbcTemplate jdbcTemplate;
  private final Integer partitionSize;

  private static final String SELECT_ALL_PARTITIONS_FOR_MATCH_TABLE =
      "SELECT pg_get_expr(pt.relpartbound, pt.oid, true) AS partition_expression "
          + "FROM pg_class base_tb "
          + "JOIN pg_inherits i ON i.inhparent = base_tb.oid "
          + "JOIN pg_class pt ON pt.oid = i.inhrelid "
          + "WHERE base_tb.oid = 'warehouse_match'::regclass";
  private static final Pattern PARTITIONING_PATTERN =
      Pattern.compile("FOR VALUES FROM \\('\\d+'\\) TO \\('(\\d+)'\\)");

  @Scheduled(cron = "${warehouse.partition.scheduledCron}")
  @EventListener(ApplicationStartedEvent.class)
  @Transactional
  public void checkAndCreatePartitions() {
    List<String> parts = getLastPartitionRange();
    log.debug("Number of warehouse_match partitions: {}", parts.size());

    Integer maxPartitionRange = getMaxPartitionRange(parts);

    if (getMaxValueForTable() + partitionSize > maxPartitionRange) {
      createPartition(maxPartitionRange, partitionSize);
    }
  }

  @NotNull
  private Integer getMaxPartitionRange(List<String> parts) {
    return parts.stream()
        .map(this::extractMaxRange)
        .max(Integer::compareTo)
        .orElse(0);
  }

  List<String> getLastPartitionRange() {
    return jdbcTemplate.query(
        SELECT_ALL_PARTITIONS_FOR_MATCH_TABLE,
        (rs, rsNum) -> rs.getString("partition_expression"));
  }

  Integer extractMaxRange(String partQuery) {
    Matcher matcher = PARTITIONING_PATTERN.matcher(partQuery);
    if (matcher.find()) {
      String maxRange = matcher.group(1);
      log.debug("Max range for the partition: {}, will try to parse value", maxRange);
      return Integer.valueOf(maxRange);
    }
    throw new IllegalStateException("Unable to parse partition range: " + partQuery);
  }

  Integer getMaxValueForTable() {
    return Optional.ofNullable(jdbcTemplate
        .queryForObject("SELECT MAX(alert_id) FROM warehouse_match",
        Map.of(), Integer.class)).orElse(0);
  }

  void createPartition(Integer maxPartitionRange, Integer partitionSize) {
    int from = maxPartitionRange + 1;
    int to = maxPartitionRange + partitionSize;
    jdbcTemplate.execute("CREATE TABLE " + "warehouse_match_" + to
        + " PARTITION OF warehouse_match FOR VALUES FROM (" + from + ") TO (" + to + ")",
        PreparedStatement::execute);
    log.info("Partition: {} created", "warehouse_match_" + to);
  }
}
