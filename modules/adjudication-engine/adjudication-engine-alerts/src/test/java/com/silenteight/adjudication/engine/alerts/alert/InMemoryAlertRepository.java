package com.silenteight.adjudication.engine.alerts.alert;

import com.silenteight.sep.base.common.support.persistence.BasicInMemoryRepository;

import java.util.List;
import java.util.Set;

class InMemoryAlertRepository extends BasicInMemoryRepository<AlertEntity>
    implements AlertRepository {

  @Override
  public int deleteAllByIdIn(List<Long> alertIds) {
    Set<Long> keySet = getInternalStore().keySet();
    int initialSizeOfKeySet = keySet.size();

    alertIds.forEach(keySet::remove);
    return initialSizeOfKeySet - keySet.size();
  }
}
