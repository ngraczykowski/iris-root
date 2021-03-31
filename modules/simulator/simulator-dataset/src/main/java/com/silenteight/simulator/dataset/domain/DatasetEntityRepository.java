package com.silenteight.simulator.dataset.domain;

import org.springframework.data.repository.Repository;

import java.util.Collection;

interface DatasetEntityRepository extends Repository<DatasetEntity, Long> {

  DatasetEntity save(DatasetEntity datasetEntity);

  Collection<DatasetEntity> findAll();

  Collection<DatasetEntity> findAllByState(DatasetState state);
}
