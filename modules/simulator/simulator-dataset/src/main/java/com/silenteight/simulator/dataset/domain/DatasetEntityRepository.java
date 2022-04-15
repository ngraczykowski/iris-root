package com.silenteight.simulator.dataset.domain;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

interface DatasetEntityRepository extends Repository<DatasetEntity, Long> {

  DatasetEntity save(DatasetEntity datasetEntity);

  Collection<DatasetEntity> findAll();

  Collection<DatasetEntity> findAllByStateIn(Collection<DatasetState> states);

  Collection<DatasetEntity> findAllByExternalResourceNameIn(
      Collection<String> externalResourceNames);

  Optional<DatasetEntity> findByDatasetId(UUID datasetId);

  @Query("SELECT d.datasetId"
      + " FROM DatasetEntity d"
      + " WHERE d.externalResourceName IN :externalResourceNames")
  Collection<UUID> findAllDatasetIdsByExternalResourceNameIn(
      Collection<String> externalResourceNames);

  @Query("SELECT SUM(d.initialAlertCount)"
      + " FROM DatasetEntity d"
      + " WHERE d.datasetId IN :datasetIds")
  long sumAlertsInDatasets(Collection<UUID> datasetIds);
}
