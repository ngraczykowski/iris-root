package com.silenteight.adjudication.engine.alerts.alert;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.alerts.alert.domain.RemoveLabelsRequest;
import com.silenteight.adjudication.engine.common.resource.ResourceName;

import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
class RemoveLabelUseCase {

  private final AlertLabelDataAccess alertLabelDataAccess;

  List<String> removeLabels(List<String> alertNames, List<String> labelsNames) {
    var alertIds =
        alertNames.stream().map(an -> ResourceName.create(an).getLong("alerts")).collect(toList());
    var request = RemoveLabelsRequest.builder().alertIds(alertIds).labelNames(labelsNames).build();

    alertLabelDataAccess.removeLabels(request);

    return alertNames;
  }
}
