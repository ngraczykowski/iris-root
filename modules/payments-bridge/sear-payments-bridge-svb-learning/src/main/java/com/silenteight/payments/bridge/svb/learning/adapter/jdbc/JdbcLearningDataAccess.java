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
  @Transactional
  public void removeFileData(List<String> fileNames) {
    removeFileCsvRowsQuery.remove(fileNames);
  }
}
