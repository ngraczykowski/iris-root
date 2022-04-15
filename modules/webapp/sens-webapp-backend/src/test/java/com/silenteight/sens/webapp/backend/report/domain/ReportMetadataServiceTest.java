package com.silenteight.sens.webapp.backend.report.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;

import static java.time.OffsetDateTime.parse;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportMetadataServiceTest {

  @InjectMocks
  private ReportMetadataService underTest;

  @Mock
  private ReportMetadataRepository repository;

  @Test
  void reportMetadataNotAvailable_startTimeIsNull() {
    // given
    String reportName = "user-auth-activity";
    when(repository.findByReportName(reportName)).thenReturn(empty());

    // when
    OffsetDateTime result = underTest.getStartTime(reportName);

    // then
    assertThat(result).isNull();
  }

  @Test
  void reportMetadataAvailable_returnStartTime() {
    // given
    String reportName = "user-auth-activity";
    OffsetDateTime startTime = parse("2020-05-28T12:42:15+01:00");
    when(repository.findByReportName(reportName))
        .thenReturn(of(new ReportMetadata(reportName, startTime)));

    // when
    OffsetDateTime result = underTest.getStartTime(reportName);

    // then
    assertThat(result).isEqualTo(startTime);
  }

  @Test
  void reportMetadataNotAvailable_create() {
    // given
    String reportName = "user-auth-activity";
    OffsetDateTime startTime = parse("2020-05-28T12:42:15+01:00");
    when(repository.findByReportName(reportName)).thenReturn(empty());

    // when
    underTest.saveStartTime(reportName, startTime);

    // then
    verify(repository).save(any(ReportMetadata.class));
  }

  @Test
  void reportMetadataAvailable_update() {
    // given
    String reportName = "user-auth-activity";
    OffsetDateTime startTime = parse("2020-05-28T12:42:15+01:00");
    when(repository.findByReportName(reportName))
        .thenReturn(of(new ReportMetadata(reportName, startTime)));

    // when
    underTest.saveStartTime(reportName, startTime);

    // then
    verify(repository).updateStartTime(reportName, startTime);
  }
}
