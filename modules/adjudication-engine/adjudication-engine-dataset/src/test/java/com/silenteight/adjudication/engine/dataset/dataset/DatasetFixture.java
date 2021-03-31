package com.silenteight.adjudication.engine.dataset.dataset;

class DatasetFixture {

  static DatasetFacade inMemoryDatasetFacade() {
    var datasetRepository = new InMemoryDatasetRepository();
    var datasetAlertRepository = new InMemoryDatasetAlertRepository();

    var createAlertsUseCase = new CreateDatasetUseCase(datasetRepository, datasetAlertRepository);

    return new DatasetFacade(createAlertsUseCase);
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
