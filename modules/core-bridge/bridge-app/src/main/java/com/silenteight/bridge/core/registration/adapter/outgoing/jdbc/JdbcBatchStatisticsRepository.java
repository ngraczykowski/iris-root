package com.silenteight.bridge.core.registration.adapter.outgoing.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.bridge.core.recommendation.domain.model.BatchWithAlertsDto.AlertStatus;
import com.silenteight.bridge.core.registration.domain.port.outgoing.BatchStatisticsRepository;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
class JdbcBatchStatisticsRepository implements BatchStatisticsRepository {

  private final CrudBatchStatisticsRepository statisticsRepository;

  @Override
  public Map<AlertStatus, Long> getAlertsStatusStatistics(String batchId) {
    return statisticsRepository.getAlertsStatusStatistics(batchId)
        .stream()
        .collect(Collectors.toMap(
            projection -> AlertStatus.valueOf(projection.status()),
            BatchStatisticProjection::count,
            (v1, v2) -> v1));
  }

  @Override
  public Map<AlertStatus, Long> getAlertsStatusStatistics(
      String batchId, List<String> alertsNames) {
    return statisticsRepository.getAlertsStatusStatistics(batchId, alertsNames)
        .stream()
        .collect(Collectors.toMap(
            projection -> AlertStatus.valueOf(projection.status()),
            BatchStatisticProjection::count,
            (v1, v2) -> v1));
  }
}
