package com.silenteight.adjudication.engine.alerts.alert;

import org.springframework.data.repository.Repository;

import java.util.List;
import javax.annotation.Nonnull;

interface AlertRepository extends Repository<AlertEntity, Long> {

  @Nonnull
  AlertEntity save(AlertEntity alertEntity);

  int deleteAllByIdIn(List<Long> alertIds);
}
