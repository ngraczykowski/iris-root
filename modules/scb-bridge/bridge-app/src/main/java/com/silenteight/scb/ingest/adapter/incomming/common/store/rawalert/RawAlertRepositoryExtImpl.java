package com.silenteight.scb.ingest.adapter.incomming.common.store.rawalert;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.scb.ingest.adapter.incomming.common.domain.GnsSyncConstants;

import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import javax.persistence.EntityManager;

@Slf4j
@AllArgsConstructor
public class RawAlertRepositoryExtImpl implements RawAlertRepositoryExt {

  private static final String CREATE_PARTITION_QUERY =
      "CREATE TABLE IF NOT EXISTS %s PARTITION OF scb_raw_alert FOR VALUES FROM ('%s') TO ('%s')";

  private static final DateTimeFormatter PARTITION_NAME_FORMATTER =
      DateTimeFormatter.ofPattern("'raw_alert_'yyyy_MM");

  private static final DateTimeFormatter RANGE_FORMATTER =
      DateTimeFormatter.ofPattern("yyyy-MM'-01'");

  private final EntityManager entityManager;

  @Transactional(GnsSyncConstants.PRIMARY_TRANSACTION_MANAGER)
  public void createPartition(OffsetDateTime dateTime) {

    var partitionName = PARTITION_NAME_FORMATTER.format(dateTime);
    var rangeFrom = RANGE_FORMATTER.format(dateTime);
    var rangeTo = RANGE_FORMATTER.format(dateTime.plusMonths(1));
    log.info(
        "Create partition: {} for records with `created_at` in range [{} - {}]", partitionName,
        rangeFrom, rangeTo);

    var query = entityManager.createNativeQuery(
        String.format(CREATE_PARTITION_QUERY, partitionName, rangeFrom, rangeTo));
    query.executeUpdate();
  }

}
