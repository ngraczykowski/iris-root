package com.silenteight.adjudication.engine.alerts.alert.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.alerts.alert.AlertLabelDataAccess;
import com.silenteight.adjudication.engine.alerts.alert.domain.InsertLabelRequest;
import com.silenteight.adjudication.engine.alerts.alert.domain.RemoveLabelsRequest;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import javax.annotation.Nonnull;

@Repository
@RequiredArgsConstructor
class JdbcAlertLabelDataAccess implements AlertLabelDataAccess {

  private final InsertAlertLabelsQuery insertAlertLabelsQuery;

  private final DeleteLabelsQuery deleteLabelsQuery;

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
}
