package com.silenteight.adjudication.engine.analysis.pii;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.common.resource.ResourceName;

import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
class RemovePiiDataUseCase {

  private final PiiDataAccess piiDataAccess;

  void removePii(List<String> alertsNames) {
    var alertIds =
        alertsNames
            .stream()
            .map(am -> ResourceName.create(am).getLong("alerts"))
            .collect(toList());

    piiDataAccess.removePiiData(alertIds);
  }
}
