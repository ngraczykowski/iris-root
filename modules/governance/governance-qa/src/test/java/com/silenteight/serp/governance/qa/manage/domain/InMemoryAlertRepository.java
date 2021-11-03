package com.silenteight.serp.governance.qa.manage.domain;

import com.silenteight.sep.base.common.support.persistence.BasicInMemoryRepository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

class InMemoryAlertRepository
    extends BasicInMemoryRepository<Alert>
    implements AlertRepository {

  @Override
  public Optional<Alert> findById(Long id) {
    return stream()
        .filter(alert -> alert.getId().equals(id))
        .findFirst();
  }

  @Override
  public Optional<Alert> findByDiscriminator(String discriminator) {
    return stream()
        .filter(alert -> alert.getDiscriminator().equals(discriminator))
        .findFirst();
  }

  @Override
  public List<Long> findIdByDiscriminatorIn(List<String> discriminators) {
    return stream()
        .filter(alert -> discriminators.contains(alert.getDiscriminator()))
        .map(Alert::getId)
        .collect(toList());
  }

  public List<Alert> findNewerThan(OffsetDateTime createdAt, Integer limit) {
    return stream()
        .filter(alert -> alert.getCreatedAt().isAfter(createdAt))
        .limit(limit)
        .collect(toList());
  }
}
