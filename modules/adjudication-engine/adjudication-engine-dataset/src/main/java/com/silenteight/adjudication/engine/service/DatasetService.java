package com.silenteight.adjudication.engine.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.v1.*;
import com.silenteight.adjudication.engine.dataset.DatasetFacade;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
class DatasetService {

  @NonNull
  private final DatasetFacade datasetFacade;

  Dataset createDataset(CreateDatasetRequest request) {
    if (request.hasNamedAlerts()) {
      return datasetFacade.createDataset(request.getNamedAlerts());
    } else if (request.hasFilteredAlerts()) {
      return datasetFacade.createDataset(request.getFilteredAlerts());
    } else {
      throw new IllegalArgumentException("Request parameters required");
    }
  }

  Dataset getDataset(GetDatasetRequest request) {
    return datasetFacade.getDataset(request.getName());
  }

  ListDatasetsResponse listDataset(ListDatasetsRequest request) {
    var page = Integer.parseInt(request.getPageToken());
    return datasetFacade.listDataset(PageRequest.of(page, request.getPageSize()));
  }

  ListDatasetAlertsResponse listDatasetAlerts(ListDatasetAlertsRequest request) {
    var page = Integer.parseInt(request.getPageToken());
    return datasetFacade.listDatasetAlerts(
        PageRequest.of(page, request.getPageSize()), request.getDataset());
  }
}
