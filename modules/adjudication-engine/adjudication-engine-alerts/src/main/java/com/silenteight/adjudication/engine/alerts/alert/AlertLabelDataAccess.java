package com.silenteight.adjudication.engine.alerts.alert;

import com.silenteight.adjudication.engine.alerts.alert.domain.InsertLabelRequest;
import com.silenteight.adjudication.engine.alerts.alert.domain.RemoveLabelsRequest;

import java.util.List;

public interface AlertLabelDataAccess {

  void insertLabels(List<InsertLabelRequest> requests);

  void removeLabels(RemoveLabelsRequest request);
}
