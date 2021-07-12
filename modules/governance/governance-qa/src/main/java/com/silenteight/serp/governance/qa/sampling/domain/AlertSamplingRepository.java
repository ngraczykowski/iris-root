package com.silenteight.serp.governance.qa.sampling.domain;

import org.springframework.data.repository.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

interface AlertSamplingRepository extends Repository<AlertSampling, Long> {

  List<AlertSampling> getAllByRangeFromAndRangeToAndStateIn(
      OffsetDateTime rangeFrom, OffsetDateTime rangeTo, List<JobState> states);

  AlertSampling save(AlertSampling alertSampling);

  List<AlertSampling> getAllByStateIn(List<JobState> states);

  Optional<AlertSampling> getById(Long id);
}
