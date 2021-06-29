package com.silenteight.serp.governance.qa.sampling.domain;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

interface AlertSamplingRepository extends Repository<AlertSampling, Long> {

  @Query("SELECT s FROM AlertSampling s "
      + "WHERE s.rangeFrom = :rangeFrom AND s.rangeTo = :rangeTo "
      + "AND s.state IN (:states)")
  List<AlertSampling> getByDateRangeAndStates(
      OffsetDateTime rangeFrom, OffsetDateTime rangeTo, List<JobState> states);

  AlertSampling save(AlertSampling alertSampling);

  @Query("SELECT s FROM AlertSampling s "
      + "WHERE state IN (:states)")
  List<AlertSampling> getByStates(List<JobState> states);

  Optional<AlertSampling> getById(Long id);
}
