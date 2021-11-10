package com.silenteight.adjudication.engine.dataset.dataset;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.common.resource.ResourceName;

import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
class GetDatasetsByAlertsUseCase {

  private final DatasetAlertDataAccess dataAccess;

  List<String> getDatasets(List<String> alertNames) {
    var datasetIds = dataAccess.selectDatasetsByAlerts(
        alertNames.stream().map(a -> ResourceName.create(a).getLong("alerts")).collect(
            toList()));
    return datasetIds.stream().map(d -> "datasets/" + d).collect(toList());
  }
}
