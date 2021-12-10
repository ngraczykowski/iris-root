package com.silenteight.adjudication.engine.alerts.alert.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.alerts.alert.AlertLabelDataAccess;
import com.silenteight.adjudication.engine.alerts.alert.domain.InsertLabelRequest;
import com.silenteight.adjudication.engine.alerts.alert.domain.RemoveLabelsRequest;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import javax.annotation.Nonnull;

import static com.silenteight.adjudication.engine.alerts.alert.jdbc.AlertQueryConstants.LABEL_NAME_SOURCE;
import static com.silenteight.adjudication.engine.alerts.alert.jdbc.AlertQueryConstants.LABEL_VALUE_LEARNING;
import static com.silenteight.adjudication.engine.alerts.alert.jdbc.AlertQueryConstants.LABEL_VALUE_SOLVING;

@Repository
@RequiredArgsConstructor
class JdbcAlertLabelDataAccess implements AlertLabelDataAccess {

  private final InsertAlertLabelsQuery insertAlertLabelsQuery;
  private final DeleteLabelsQuery deleteLabelsQuery;
  private final CountAlertLabelsByNameAndValueQuery countAlertLabelsByNameAndValueQuery;
  private final CountAlertLabelsSubsetInSet countAlertLabelsSubsetInSet;

  @Override
  @Transactional
  public void insertLabels(@Nonnull List<InsertLabelRequest> requests) {
    insertAlertLabelsQuery.insert(requests);
  }

  @Override
  @Transactional
  public void removeLabels(RemoveLabelsRequest request) {
    deleteLabelsQuery.execute(request.getAlertIds(), request.getLabelNames());
  }

  @Override
  public long countByNameAndValue(String name, String value) {
    return countAlertLabelsByNameAndValueQuery.execute(name, value);
  }

  @Override
  public long countAlertsLearningInSolvingSet() {
    return countAlertLabelsSubsetInSet.execute(
        LABEL_NAME_SOURCE, LABEL_VALUE_LEARNING,
        LABEL_NAME_SOURCE, LABEL_VALUE_SOLVING);
  }

  @Override
  public long countAlertsSolvingInLearningSet() {
    return countAlertLabelsSubsetInSet.execute(
        LABEL_NAME_SOURCE, LABEL_VALUE_SOLVING,
        LABEL_NAME_SOURCE, LABEL_VALUE_LEARNING);
  }
}
