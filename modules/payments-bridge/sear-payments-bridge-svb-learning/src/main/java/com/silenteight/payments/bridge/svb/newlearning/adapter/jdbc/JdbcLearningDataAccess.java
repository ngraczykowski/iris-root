package com.silenteight.payments.bridge.svb.newlearning.adapter.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.svb.newlearning.domain.AlertsReadingResponse;
import com.silenteight.payments.bridge.svb.newlearning.port.LearningDataAccess;
import com.silenteight.payments.bridge.svb.newlearning.step.etl.LearningProcessedAlert;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
class JdbcLearningDataAccess implements LearningDataAccess {

  private final SelectProcessedAlertsStatusQuery selectProcessedAlertsStatusQuery;
  private final InsertAlertResultQuery insertAlertResultQuery;

  @Override
  public AlertsReadingResponse select(long jobId, String fileName) {
    return selectProcessedAlertsStatusQuery.select(jobId, fileName);
  }

  @Override
  @Transactional
  public void saveResult(List<LearningProcessedAlert> alerts) {
    insertAlertResultQuery.update(alerts);
  }
}
