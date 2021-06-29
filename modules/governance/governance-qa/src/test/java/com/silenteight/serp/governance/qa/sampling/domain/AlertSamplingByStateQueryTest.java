package com.silenteight.serp.governance.qa.sampling.domain;

import com.silenteight.serp.governance.qa.sampling.domain.dto.AlertSamplingDto;
import com.silenteight.serp.governance.qa.sampling.domain.dto.DateRangeDto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.List;

import static com.silenteight.serp.governance.qa.sampling.domain.JobState.FAILED;
import static com.silenteight.serp.governance.qa.sampling.domain.JobState.FINISHED;
import static com.silenteight.serp.governance.qa.sampling.domain.JobState.STARTED;
import static java.time.OffsetDateTime.parse;
import static org.assertj.core.api.Assertions.*;

class AlertSamplingByStateQueryTest {

  private InMemoryAlertSamplingRepository inMemoryAlertSamplingRepository;
  private AlertSamplingByStateQuery underTest;

  @BeforeEach
  void setUp() {
    inMemoryAlertSamplingRepository = new InMemoryAlertSamplingRepository();
    underTest = new SamplingDomainConfiguration()
        .alertSamplingQuery(inMemoryAlertSamplingRepository);
  }

  @Test
  void listFinishedShouldReturnFinishedAlertSamplingForCurrentMonth() {
    //given
    DateRangeDto dateRangeEarlier = new DateRangeDto(
        parse("2021-05-01T01:01:00.0000+02:00"), parse("2021-05-31T01:01:00.0000+02:00"));
    DateRangeDto dateRangeCurrent = new DateRangeDto(
        parse("2021-06-01T01:01:00.0000+02:00"), parse("2021-06-30T01:01:00.0000+02:00"));
    inMemoryAlertSamplingRepository.save(
        getAlertSampling(dateRangeEarlier, null, STARTED));
    inMemoryAlertSamplingRepository.save(
        getAlertSampling(dateRangeEarlier, parse("2021-05-01T01:01:00.0000+02:00"), FINISHED));
    inMemoryAlertSamplingRepository.save(
        getAlertSampling(dateRangeCurrent, parse("2021-06-01T01:01:00.0000+02:00"), FINISHED));
    inMemoryAlertSamplingRepository.save(
        getAlertSampling(dateRangeCurrent, null, STARTED));
    inMemoryAlertSamplingRepository.save(
        getAlertSampling(dateRangeCurrent, parse("2021-06-01T01:01:00.0000+02:00"), FAILED));
    DateRangeDto dateRangeDto = new DateRangeDto(
        parse("2021-06-01T01:01:00.0000+02:00"), parse("2021-06-30T01:01:00.0000+02:00"));
    //when
    List<AlertSamplingDto> alertSamplingDtos = underTest.listFinished(dateRangeDto);
    //then
    assertThat(alertSamplingDtos.size()).isEqualTo(1);
    alertSamplingDtos.forEach(alertSamplingDto -> {
      assertThat(alertSamplingDto.getState()).isIn(FINISHED);
      assertThat(alertSamplingDto.getRangeFrom()).isEqualTo(dateRangeCurrent.getFrom());
      assertThat(alertSamplingDto.getRangeTo()).isEqualTo(dateRangeCurrent.getTo());
    });
  }

  private AlertSampling getAlertSampling(DateRangeDto dateRangeDto, OffsetDateTime finishedAt,
      JobState state) {

    AlertSampling alertSampling = new AlertSampling();
    alertSampling.setState(state);
    alertSampling.setStartedAt(dateRangeDto.getFrom());
    alertSampling.setRangeFrom(dateRangeDto.getFrom());
    alertSampling.setRangeTo(dateRangeDto.getTo());
    alertSampling.setFinishedAt(finishedAt);
    return alertSampling;
  }
}
