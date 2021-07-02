package com.silenteight.serp.governance.qa.sampling.job;

import com.silenteight.serp.governance.qa.sampling.domain.AlertSamplingByStateQuery;
import com.silenteight.serp.governance.qa.sampling.domain.AlertSamplingService;
import com.silenteight.serp.governance.qa.sampling.domain.dto.DateRangeDto;
import com.silenteight.serp.governance.qa.sampling.generator.AlertsGeneratorService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;

import static com.silenteight.sep.base.testing.time.MockTimeSource.ARBITRARY_INSTANCE;
import static java.time.OffsetDateTime.parse;
import static java.util.List.of;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlertsGeneratorTest {

  @Mock
  AlertSamplingService alertSamplingService;
  @Mock
  AlertsGeneratorService alertsGeneratorService;
  @Mock
  AlertSamplingByStateQuery alertSamplingQuery;
  @Mock
  DateRangeProvider dateRangeProvider;
  @Mock
  CronExecutionTimeProvider cronExecutionTimeProvider;

  private AlertsGenerator underTest;

  @BeforeEach
  void beforeEach() {
    underTest = new AlertsGenerator(
        alertSamplingQuery,
        alertSamplingService,
        alertsGeneratorService,
        dateRangeProvider,
        cronExecutionTimeProvider,
        ARBITRARY_INSTANCE);
  }

  @Test
  void generateAlertsIfNeededShouldStartJobAndGenerateAlertsAndFinishJob() {
    //given
    DateRangeDto dateRangeDto = new DateRangeDto(parse("2021-05-01T01:00:00Z"),
        parse("2021-05-31T01:00:00Z"));
    OffsetDateTime startedAt = parse("2021-06-01T01:00:00Z");
    //when
    when(cronExecutionTimeProvider.executionTime()).thenReturn(startedAt);
    when(dateRangeProvider.latestDateRange()).thenReturn(dateRangeDto);
    when(alertSamplingQuery.listFinished(dateRangeDto)).thenReturn(of());
    when(alertSamplingService.createAlertsSampling(dateRangeDto, startedAt)).thenReturn(1L);
    underTest.generateAlertsIfNeeded();
    //then
    verify(alertSamplingService, atLeastOnce()).failLongRunningTasks(startedAt);
    verify(alertSamplingService, atLeastOnce()).createAlertsSampling(dateRangeDto, startedAt);
    verify(alertSamplingService, atLeastOnce()).finish(any());
    verify(alertsGeneratorService, atLeastOnce()).generateAlerts(dateRangeDto, 1L);
    verify(cronExecutionTimeProvider, atLeastOnce()).executionTime();
  }

  @Test
  void generateAlertsIfNeededShouldStartJobAndMarkAsFailed_whenRuntimeExceptionThrown() {
    //given
    OffsetDateTime startedAt = parse("2021-06-01T01:00:00Z");
    DateRangeDto dateRangeDto = new DateRangeDto(parse("2021-05-01T01:00:00Z"),
        parse("2021-05-31T01:00:00Z"));
    ArgumentCaptor<DateRangeDto> dateRangeCaptor = ArgumentCaptor.forClass(DateRangeDto.class);
    ArgumentCaptor<Long> alertSamplingIdCaptor = ArgumentCaptor.forClass(Long.class);
    ArgumentCaptor<OffsetDateTime> startedAtCaptor = ArgumentCaptor.forClass(OffsetDateTime.class);
    //when
    when(cronExecutionTimeProvider.executionTime()).thenReturn(startedAt);
    when(dateRangeProvider.latestDateRange()).thenReturn(dateRangeDto);
    when(alertSamplingQuery.listFinished(dateRangeDto)).thenReturn(of());
    when(alertSamplingService.createAlertsSampling(dateRangeDto, startedAt)).thenReturn(1L);
    doThrow(RuntimeException.class).when(alertsGeneratorService)
        .generateAlerts(dateRangeDto, 1L);
    underTest.generateAlertsIfNeeded();
    //then
    verify(alertSamplingService, times(1))
        .createAlertsSampling(dateRangeCaptor.capture(), startedAtCaptor.capture());
    verify(alertsGeneratorService, times(1))
        .generateAlerts(dateRangeCaptor.capture(), alertSamplingIdCaptor.capture());
    verify(alertSamplingService, times(1)).markAsFailed(1L);
  }
}
