package com.silenteight.adjudication.engine.dataset.dataset;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.*;
import com.silenteight.adjudication.engine.common.resource.ResourceName;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.validation.Valid;

@RequiredArgsConstructor
@Service
public class DatasetFacade {

  @NonNull
  private final CreateDatasetUseCase createDatasetUseCase;

  public Dataset createDataset(@Valid NamedAlerts namedAlerts) {
    return createDatasetUseCase.createDataset(namedAlerts);
  }

  public Dataset createDataset(@Valid FilteredAlerts filteredAlerts) {
    return createDatasetUseCase.createDataset(filteredAlerts);
  }

  public Dataset getDataset(String name) {
    return createDatasetUseCase.getDataset(name);
  }

  public ListDatasetsResponse listDataset(Pageable pageable) {
    var page = createDatasetUseCase.listDataset(pageable);
    String pageToken = page.hasNext() ? String.valueOf(page.nextPageable().getPageNumber()) : "";
    return ListDatasetsResponse.newBuilder()
        .addAllDatasets(page.toList())
        .setNextPageToken(pageToken)
        .build();
  }

  public ListDatasetAlertsResponse listDatasetAlerts(Pageable pageable, String datasetName) {
    var page =
        createDatasetUseCase.listDatasetAlerts(pageable, ResourceName.getResource(datasetName)
            .getId("datasets"));
    String pageToken = page.hasNext() ? String.valueOf(page.nextPageable().getPageNumber()) : "";
    return ListDatasetAlertsResponse.newBuilder()
        .addAllDatasetAlertNames(page.toList())
        .setNextPageToken(pageToken)
        .build();
  }
}
