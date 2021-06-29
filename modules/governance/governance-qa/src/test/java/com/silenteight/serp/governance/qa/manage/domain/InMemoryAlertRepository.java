package com.silenteight.serp.governance.qa.manage.domain;

import com.silenteight.sep.base.common.support.persistence.BasicInMemoryRepository;
import com.silenteight.serp.governance.qa.manage.domain.exception.WrongAlertNameException;

import java.time.OffsetDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

class InMemoryAlertRepository
    extends BasicInMemoryRepository<Alert>
    implements AlertRepository {

  @Override
  public Alert findById(Long id) {
    return stream()
        .filter(alert -> alert.getId().equals(id))
        .findFirst().orElse(null);
  }

  @Override
  public Alert findByAlertName(String alertName) {
    return stream()
        .filter(alert -> alert.getAlertName().equals(alertName))
        .findFirst()
        .orElseThrow(() -> new WrongAlertNameException(alertName));
  }

  public List<Alert> findNewerThan(OffsetDateTime createdAt, Integer limit) {
    return stream()
        .filter(alert -> alert.getCreatedAt().isAfter(createdAt))
        .limit(limit)
        .collect(toList());
  }
}
