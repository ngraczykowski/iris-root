package com.silenteight.hsbc.bridge.adjudication;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.CreateDatasetRequest;
import com.silenteight.adjudication.api.v1.NamedAlerts;

import java.util.List;

@RequiredArgsConstructor
class DatasetService {

  private final AdjudicationApi adjudicationApi;

  String createDataset(List<String> alertIds) {
    NamedAlerts namedAlerts = NamedAlerts.newBuilder()
        .addAllAlerts(alertIds)
        .build();

    var createDatasetRequest = CreateDatasetRequest.newBuilder()
        .setNamedAlerts(namedAlerts)
        .build();

    var dataset = adjudicationApi.createDataset(createDatasetRequest);

    return dataset.getName();
  }

}
