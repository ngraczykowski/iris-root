package com.silenteight.adjudication.engine.dataset.dataset;

class DatasetFixture {

  static DatasetFacade inMemoryDatasetFacade() {
    var datasetRepository = new InMemoryDatasetRepository();
    var datasetAlertRepository = new InMemoryDatasetAlertRepository();
    var datasetAlertDataAccess = new InMemoryDatasetAlertDataAccess();

    var createAlertsUseCase =
        new CreateDatasetUseCase(datasetRepository, datasetAlertRepository, datasetAlertDataAccess);
    var getDatasetsUseCase = new GetDatasetsByAlertsUseCase(datasetAlertDataAccess);

    return new DatasetFacade(createAlertsUseCase, getDatasetsUseCase);
  }

  public static DatasetEntity randomDatasetEntity() {
    return DatasetEntity.builder()
        .build();
  }

  public static DatasetAlertEntity newDatasetAlertEntity(DatasetAlertKey datasetAlertId) {
    return DatasetAlertEntity
        .builder()
        .id(datasetAlertId)
        .build();
  }
}
