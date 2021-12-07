package com.silenteight.adjudication.engine.alerts.alert;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.alerts.alert.domain.InsertLabelRequest;
import com.silenteight.adjudication.engine.common.resource.ResourceName;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
class AddLabelsUseCase {

  private final AlertLabelDataAccess alertLabelDataAccess;

  Map<String, String> addLabels(List<String> alertNames, Map<String, String> labels) {
    var requests = new ArrayList<InsertLabelRequest>();
    alertNames
        .forEach(a -> labels.forEach(
            (key, value) -> requests.add(InsertLabelRequest
                .builder()
                .alertId(ResourceName.create(a).getLong("alerts"))
                .labelName(key)
                .labelValue(value)
                .build())));
    alertLabelDataAccess.insertLabels(requests);

    return labels;
  }
}
