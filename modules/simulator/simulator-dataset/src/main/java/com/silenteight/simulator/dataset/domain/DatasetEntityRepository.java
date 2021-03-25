package com.silenteight.simulator.dataset.domain;

import org.springframework.data.repository.Repository;

interface DatasetEntityRepository extends Repository<DatasetEntity, Long> {

  DatasetEntity save(DatasetEntity datasetEntity);
}
