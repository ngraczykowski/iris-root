package com.silenteight.adjudication.engine.dataset;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

import java.util.Optional;

interface DatasetRepository extends Repository<DatasetEntity, Long> {

  DatasetEntity save(DatasetEntity entity);

  Optional<DatasetEntity> findById(Long id);

  Page<DatasetEntity> findAll(Pageable pageable);
}
