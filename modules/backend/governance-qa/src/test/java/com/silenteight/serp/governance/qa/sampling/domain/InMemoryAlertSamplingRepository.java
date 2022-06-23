package com.silenteight.serp.governance.qa.sampling.domain;

import com.silenteight.sep.base.common.support.persistence.BasicInMemoryRepository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

class InMemoryAlertSamplingRepository
    extends BasicInMemoryRepository<AlertSampling>
    implements AlertSamplingRepository {

  @Override
  public List<AlertSampling> getAllByRangeFromAndRangeToAndStateIn(
      OffsetDateTime rangeFrom, OffsetDateTime rangeTo, List<JobState> states) {

    return stream()
        .filter(alertSampling -> alertSampling.getRangeFrom().equals(rangeFrom) &&
            alertSampling.getRangeTo().equals(rangeTo) &&
            states.contains(alertSampling.getState()))
        .collect(toList());
  }

  @Override
  public List<AlertSampling> getAllByStateIn(List<JobState> states) {
    return stream()
        .filter(alertSampling -> states.contains(alertSampling.getState()))
        .collect(toList());
  }

  @Override
  public Optional<AlertSampling> getById(Long id) {
    return stream()
        .filter(alert -> alert.getId().equals(id))
        .findFirst();
  }
}
