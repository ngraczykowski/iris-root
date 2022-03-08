package com.silenteight.scb.ingest.adapter.incomming.common.health;

import com.silenteight.scb.ingest.adapter.incomming.cbs.quartz.QueuingJobProperties;
import com.silenteight.scb.ingest.adapter.incomming.cbs.quartz.QueuingJobsProperties;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;

import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

class DatabaseQueuingJobsConsistencyHealthIndicator extends DatabaseJobsConsistencyHealthIndicator
    implements HealthIndicator {

  private QueuingJobsProperties queuingJobsProperties;

  DatabaseQueuingJobsConsistencyHealthIndicator(
      DataSource dataSource, QueuingJobsProperties queuingJobsProperties) {
    super(dataSource);
    this.queuingJobsProperties = queuingJobsProperties;
  }

  @Override
  public Health health() {
    Map<String, String> result = new HashMap<>();

    queuingJobsProperties.getJobs().stream()
        .filter(QueuingJobProperties::isEnabled)
        .forEach(job -> verifyJob(job, result));

    if (result.containsValue(NOT_PRESENT) || result.containsValue(DB_ERROR)) {
      return Health.down().withDetails(result).build();
    } else {
      return Health.up().withDetails(result).build();
    }
  }

  private void verifyJob(QueuingJobProperties job, Map<String, String> result) {
    result.putAll(
        verifyIfTableExists("QueuingJobs", job.getRecordsView()));

    String cbsHitsDetailsHelperViewName = job.getHitDetailsView();
    if (isNotBlank(cbsHitsDetailsHelperViewName)) {
      result.putAll(verifyIfTableExists("QueuingJobs", cbsHitsDetailsHelperViewName));
    }
  }
}
