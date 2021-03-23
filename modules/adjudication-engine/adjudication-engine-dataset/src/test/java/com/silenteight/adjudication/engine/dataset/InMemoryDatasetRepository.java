package com.silenteight.adjudication.engine.dataset;

import com.silenteight.sep.base.common.support.persistence.BasicInMemoryRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.stream.Collectors;

class InMemoryDatasetRepository extends BasicInMemoryRepository<DatasetEntity>
    implements DatasetRepository {


  @Override
  public Optional<DatasetEntity> findById(Long id) {
    return Optional.ofNullable(getInternalStore().getOrDefault(id, null));
  }

  @Override
  public Page<DatasetEntity> findAll(
      Pageable pageable) {
    var pageNumber = pageable.getPageNumber();
    var pageSize = pageable.getPageSize();

    var list =
        stream().skip(pageNumber * pageSize).limit(pageSize).collect(Collectors.toList());
    var total = stream().count();
    return new PageImpl<>(list, pageable, total);
  }
}
