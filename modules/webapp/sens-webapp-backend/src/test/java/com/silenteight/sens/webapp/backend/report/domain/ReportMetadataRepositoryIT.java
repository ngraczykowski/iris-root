package com.silenteight.sens.webapp.backend.report.domain;

import com.silenteight.sep.base.testing.BaseDataJpaTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;

import java.time.OffsetDateTime;
import java.util.Optional;

import static java.math.BigInteger.valueOf;
import static java.time.OffsetDateTime.parse;
import static org.assertj.core.api.Assertions.*;

@TestPropertySource("classpath:data-test.properties")
class ReportMetadataRepositoryIT extends BaseDataJpaTest {

  private static final String REPORT_NAME = "REPORT";

  @Autowired
  private ReportMetadataRepository repository;

  @Test
  void reportMetadataSaved() {
    // given
    OffsetDateTime startTime = parse("2020-05-20T10:15:30+01:00");
    ReportMetadata reportMetadata = new ReportMetadata(REPORT_NAME, startTime);

    // when
    repository.save(reportMetadata);

    // then
    Object count = entityManager
        .getEntityManager()
        .createNativeQuery("SELECT count(report_metadata_id) from webapp_report_metadata")
        .getSingleResult();
    assertThat(count).isEqualTo(valueOf(1));
  }

  @Test
  void findByReportName() {
    // given
    OffsetDateTime startTime = parse("2020-05-20T10:15:30+01:00");
    ReportMetadata reportMetadata = new ReportMetadata(REPORT_NAME, startTime);
    entityManager.persistAndFlush(reportMetadata);

    // when
    Optional<ReportMetadata> result = repository.findByReportName(REPORT_NAME);

    // then
    assertThat(result).isNotEmpty();
    assertThat(result.get()).isEqualTo(reportMetadata);
  }
}
