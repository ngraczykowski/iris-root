package com.silenteight.adjudication.engine.alerts.alert;

import org.springframework.data.repository.Repository;

import javax.annotation.Nonnull;

interface AlertRepository extends Repository<AlertEntity, Long> {

  @Nonnull
  AlertEntity save(AlertEntity alertEntity);
}
