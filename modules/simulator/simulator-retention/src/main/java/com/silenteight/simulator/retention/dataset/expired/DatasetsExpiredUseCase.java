package com.silenteight.simulator.retention.dataset.expired;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.dataretention.api.v1.AnalysisExpired;
import com.silenteight.dataretention.api.v1.DatasetsExpired;
import com.silenteight.simulator.dataset.domain.DatasetMetadataService;
import com.silenteight.simulator.dataset.domain.DatasetQuery;
import com.silenteight.simulator.management.list.ListSimulationsQuery;
import com.silenteight.simulator.retention.dataset.amqp.listener.DatasetsExpiredMessageHandler;

import java.util.Collection;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class DatasetsExpiredUseCase implements DatasetsExpiredMessageHandler {

  @NonNull
  private final DatasetMetadataService datasetMetadataService;
  @NonNull
  private final DatasetQuery datasetQuery;
  @NonNull
  private final ListSimulationsQuery listSimulationsQuery;

  @Override
  public AnalysisExpired handle(@NonNull DatasetsExpired request) {
    List<String> externalResourceNames = request.getDatasetsList();
    log.info("Expired datasets: externalResourceNames={}" + externalResourceNames);

    datasetMetadataService.expire(externalResourceNames);

    log.info("Marked all datasets as expired: " + externalResourceNames);

    Collection<String> datasetNames = datasetQuery.getDatasetNames(externalResourceNames);
    Collection<String> analysisNames = listSimulationsQuery.getAnalysisNames(datasetNames);

    log.info("Expired simulations: analysisNames={}" + analysisNames);

    return AnalysisExpired.newBuilder()
        .addAllAnalysis(analysisNames)
        .build();
  }
}
