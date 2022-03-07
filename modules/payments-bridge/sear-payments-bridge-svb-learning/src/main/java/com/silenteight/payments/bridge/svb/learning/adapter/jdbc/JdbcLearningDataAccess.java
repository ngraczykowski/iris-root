package com.silenteight.payments.bridge.svb.learning.adapter.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.svb.learning.domain.AlertsReadingResponse;
import com.silenteight.payments.bridge.svb.learning.port.LearningDataAccess;
import com.silenteight.payments.bridge.svb.learning.step.etl.LearningProcessedAlert;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
class JdbcLearningDataAccess implements LearningDataAccess {

  private final SelectProcessedAlertsStatusQuery selectProcessedAlertsStatusQuery;
  private final InsertAlertResultQuery insertAlertResultQuery;
  private final CheckIsFileStoredQuery checkIsFileStoredQuery;

  private final List<RemoveDuplicatedQuery> removeDuplicatedQueries;

  private final RemoveFileCsvRowsQuery removeFileCsvRowsQuery;
  private final RemoveLearningAlertsQuery removeLearningAlertsQuery;
  private final RemoveHitsQuery removeHitsQuery;
  private final RemoveActionsQuery removeActionsQuery;

  @Override
  public AlertsReadingResponse select(long jobId, String fileName) {
    return selectProcessedAlertsStatusQuery.select(jobId, fileName);
  }

  @Override
  @Transactional
  public void saveResult(List<LearningProcessedAlert> alerts) {
    insertAlertResultQuery.update(alerts);
  }

  @Override
  public boolean isFileStored(String fileName) {
    return checkIsFileStoredQuery.execute(fileName);
  }

  @Override
  @Transactional
  public void removeDuplicates() {
    removeDuplicatedQueries.forEach(RemoveDuplicatedQuery::remove);
  }

  @Override
  public void removeCsvRows(List<Long> rowIds) {
    removeFileCsvRowsQuery.remove(rowIds);
  }

  @Override
  public void removeAlerts(List<Long> alertIds) {
    removeLearningAlertsQuery.remove(alertIds);
  }

  @Override
  public void removeHits(List<Long> hitIds) {
    removeHitsQuery.remove(hitIds);
  }

  @Override
  public void removeActions(List<Long> actionIds) {
    removeActionsQuery.remove(actionIds);
  }
}
