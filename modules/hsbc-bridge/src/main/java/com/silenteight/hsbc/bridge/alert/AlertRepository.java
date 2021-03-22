package com.silenteight.hsbc.bridge.alert;

import org.springframework.data.repository.Repository;

import java.util.Collection;
import java.util.List;

interface AlertRepository extends Repository<AlertEntity, Long> {

  void save(AlertEntity alertEntity);

  List<AlertEntity> findByIdIn(Collection<Long> alertIds);
}
