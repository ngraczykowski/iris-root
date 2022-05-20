package com.silenteight.warehouse.report.persistence;

import org.springframework.data.repository.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

interface ReportRepository extends Repository<Report, Long> {

  Report save(Report report);

  Optional<Report> getById(Long id);

  List<Report> getAllByCreatedAtBefore(OffsetDateTime offsetDateTime);

  void deleteById(Long id);
}
