package com.silenteight.adjudication.engine.alerts.alert;

import com.silenteight.sep.base.common.support.persistence.BasicInMemoryRepository;

import org.jetbrains.annotations.NotNull;

class InMemoryAlertRepository extends BasicInMemoryRepository<AlertEntity>
    implements AlertRepository {

  @NotNull
  @Override
  public AlertEntity save(AlertEntity entity) {
    entity.initialize();
    return super.save(entity);
  }
}
