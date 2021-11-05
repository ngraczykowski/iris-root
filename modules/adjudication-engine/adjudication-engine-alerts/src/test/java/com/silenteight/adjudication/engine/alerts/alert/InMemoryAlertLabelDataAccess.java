package com.silenteight.adjudication.engine.alerts.alert;

import com.silenteight.adjudication.engine.alerts.alert.domain.InsertLabelRequest;
import com.silenteight.adjudication.engine.alerts.alert.domain.RemoveLabelsRequest;

import java.util.ArrayList;
import java.util.List;

class InMemoryAlertLabelDataAccess implements AlertLabelDataAccess {

  private static List<InsertLabelRequest> labels = new ArrayList<>();

  @Override
  public void insertLabels(List<InsertLabelRequest> requests) {
    for (var request : requests) {
      var exists = labels
          .stream()
          .filter(l -> l.getAlertId().equals(request.getAlertId()) && l
              .getLabelName()
              .equals(request.getLabelName()))
          .findAny();
      if (exists.isPresent())
        throw new RuntimeException();
    }

    labels.addAll(requests);
  }

  @Override
  public void removeLabels(
      RemoveLabelsRequest request) {

  }
}
