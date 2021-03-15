package com.silenteight.hsbc.bridge.alert;

import org.springframework.data.repository.Repository;

interface AlertRepository extends Repository<AlertEntity, Long> {

  void save(AlertEntity alertEntity);
}
