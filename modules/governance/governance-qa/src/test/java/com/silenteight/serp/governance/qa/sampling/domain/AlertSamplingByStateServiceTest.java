package com.silenteight.serp.governance.qa.sampling.domain;

import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.sep.base.testing.time.MockTimeSource;
import com.silenteight.serp.governance.qa.sampling.domain.dto.DateRangeDto;
import com.silenteight.serp.governance.qa.sampling.domain.exception.WrongAlertSamplingIdException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.Optional;

import static com.silenteight.serp.governance.qa.sampling.domain.JobState.FAILED;
import static com.silenteight.serp.governance.qa.sampling.domain.JobState.FINISHED;
import static com.silenteight.serp.governance.qa.sampling.domain.JobState.STARTED;
import static java.lang.String.format;
import static java.time.OffsetDateTime.parse;
import static org.assertj.core.api.Assertions.*;

class AlertSamplingByStateServiceTest {

  private AlertSamplingService underTest;
  private AlertSamplingRepository alertSamplingRepository;
  private final TimeSource timeSource = MockTimeSource.ARBITRARY_INSTANCE;

  @BeforeEach
  void setUp() {
    alertSamplingRepository = new InMemoryAlertSamplingRepository();
    underTest = new AlertSamplingService(alertSamplingRepository, timeSource);
  }

  @Test
  void startJobShouldSaveNewSamplingWithStartedAtAndDateRange() {
    //given
    OffsetDateTime startedAt = parse("2021-06-01T01:00:00Z");
    DateRangeDto currentDateRange = getDateRangeDto("2021-05-01T01:00:00Z", "2021-05-31T01:00:00Z");
    //when
    Long jobId = underTest.createAlertsSampling(currentDateRange, startedAt);
    Optional<AlertSampling> alertSampling = alertSamplingRepository.getById(jobId);
    //then
    assertThat(alertSampling.isPresent()).isTrue();
    assertThat(alertSampling.get().getState()).isEqualTo(STARTED);
    assertThat(alertSampling.get().getRangeFrom()).isEqualTo(currentDateRange.getFrom());
    assertThat(alertSampling.get().getRangeTo()).isEqualTo(currentDateRange.getTo());
    assertThat(alertSampling.get().getStartedAt()).isEqualTo(startedAt);
    assertThat(alertSampling.get().getFinishedAt()).isNull();
  }

  private DateRangeDto getDateRangeDto(String from, String to) {
    return new DateRangeDto(parse(from), parse(to));
  }

  @Test
  void finishJobShouldUpdateStateAndFinishedAt() {
    //given
    DateRangeDto dateRangeDto = getDateRangeDto("2021-05-01T01:00:00Z", "2021-05-31T01:00:00Z");
    AlertSampling alertSampling = getAlertSampling(dateRangeDto, STARTED);
    alertSampling = alertSamplingRepository.save(alertSampling);
    assertThat(alertSampling.getFinishedAt()).isNull();
    //when
    underTest.finish(alertSampling.getId());
    //then
    assertThat(alertSampling.getState()).isEqualTo(FINISHED);
    assertThat(alertSampling.getFinishedAt()).isEqualTo(timeSource.offsetDateTime());
    assertThat(alertSampling.getRangeFrom()).isEqualTo(dateRangeDto.getFrom());
    assertThat(alertSampling.getRangeTo()).isEqualTo(dateRangeDto.getTo());
  }

  AlertSampling getAlertSampling(DateRangeDto dateRangeDto, JobState state) {
    AlertSampling alertSampling = new AlertSampling();
    alertSampling.setRangeFrom(dateRangeDto.getFrom());
    alertSampling.setRangeTo(dateRangeDto.getTo());
    alertSampling.setState(state);
    return alertSampling;
  }

  @Test
  void finishJobShouldThrowExceptionWhenCronNotFound() {
    assertThatThrownBy(() -> underTest.finish(0L))
        .isInstanceOf(WrongAlertSamplingIdException.class)
        .hasMessageContaining(format("Could not find alertSampling with id=%d", 0L));
  }

  @Test
  void markAsFailedShouldSaveFailedState() {
    DateRangeDto dateRangeDto = getDateRangeDto("2021-05-01T01:00:00Z", "2021-05-31T01:00:00Z");
    AlertSampling alertSampling = getAlertSampling(dateRangeDto, STARTED);
    alertSampling.setState(STARTED);
    alertSampling = alertSamplingRepository.save(alertSampling);
    underTest.markAsFailed(alertSampling.getId());
    Optional<AlertSampling> foundAlertSampling = alertSamplingRepository
        .getById(alertSampling.getId());
    assertThat(foundAlertSampling.isPresent()).isTrue();
    assertThat(foundAlertSampling.get().getState()).isEqualTo(FAILED);
  }

  @Test
  void failLongRunningTasksShouldSetFailedState() {
    //given
    OffsetDateTime executionTimeBefore = parse("2021-06-01T01:00:00Z");
    OffsetDateTime executionTimeCurrent = parse("2021-06-02T01:00:00Z");

    DateRangeDto dateRangeDto = getDateRangeDto("2021-05-01T01:00:00Z", "2021-05-31T01:00:00Z");
    AlertSampling startedBeforeAlertSampling = getAlertSampling(dateRangeDto, STARTED);
    startedBeforeAlertSampling.setStartedAt(executionTimeBefore);
    startedBeforeAlertSampling = alertSamplingRepository.save(startedBeforeAlertSampling);

    AlertSampling finishedBeforeAlertSampling = getAlertSampling(dateRangeDto, FINISHED);
    finishedBeforeAlertSampling = alertSamplingRepository.save(finishedBeforeAlertSampling);
    finishedBeforeAlertSampling.setStartedAt(executionTimeBefore);

    AlertSampling startedCurrentAlertSampling = getAlertSampling(dateRangeDto, STARTED);
    startedCurrentAlertSampling.setStartedAt(executionTimeCurrent);
    startedCurrentAlertSampling = alertSamplingRepository.save(startedCurrentAlertSampling);
    //when
    underTest.failLongRunningTasks(executionTimeCurrent);
    //then
    assertThat(startedBeforeAlertSampling.getState()).isEqualTo(FAILED);
    assertThat(finishedBeforeAlertSampling.getState()).isEqualTo(FINISHED);
    assertThat(startedCurrentAlertSampling.getState()).isEqualTo(STARTED);
  }
}
