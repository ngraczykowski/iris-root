package com.silenteight.sens.webapp.backend.report.domain;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Optional;

interface ReportMetadataRepository extends Repository<ReportMetadata, Long> {

  ReportMetadata save(ReportMetadata reportMetadata);

  @Modifying
  @Transactional
  @Query("UPDATE ReportMetadata SET startTime = :starTime WHERE reportName = :reportName")
  int updateStartTime(
      @Param("reportName") String reportName,
      @Param("starTime") OffsetDateTime starTime);

  Optional<ReportMetadata> findByReportName(String reportName);
}
