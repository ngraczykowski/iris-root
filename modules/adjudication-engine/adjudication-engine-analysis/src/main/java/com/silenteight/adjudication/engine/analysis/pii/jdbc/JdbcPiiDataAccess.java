package com.silenteight.adjudication.engine.analysis.pii.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.analysis.pii.PiiDataAccess;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
class JdbcPiiDataAccess implements PiiDataAccess {

  private final RemoveMatchFeatureValueReasonQuery removeMatchFeatureValueReasonQuery;
  private final RemoveAlertCommentInputValueQuery removeAlertCommentInputValueQuery;
  private final RemoveRecommendationMatchContextQuery removeRecommendationMatchContextQuery;

  @Override
  public void removePiiData(List<Long> alertsIds) {
    removeRecommendationMatchContextQuery.execute(alertsIds);
    removeAlertCommentInputValueQuery.execute(alertsIds);
    removeMatchFeatureValueReasonQuery.execute(alertsIds);
  }
}
