package com.silenteight.hsbc.bridge.alert;

import org.springframework.data.repository.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

interface AlertRepository extends Repository<AlertEntity, Long> {

  void save(AlertEntity alertEntity);

  List<AlertEntity> findByIdIn(Collection<Long> alertIds);

  Optional<AlertEntity> findByName(String name);

  Optional<AlertEntity> findById(Long id);
}
