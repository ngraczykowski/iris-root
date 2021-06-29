package com.silenteight.serp.governance.qa.sampling.domain.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import com.silenteight.serp.governance.qa.sampling.domain.JobState;

import java.time.OffsetDateTime;


@Builder
@Data
public class AlertSamplingDto {

  @NonNull
  Long id;
  @NonNull
  JobState state;
  @NonNull
  OffsetDateTime rangeFrom;
  @NonNull
  OffsetDateTime rangeTo;
  @NonNull
  OffsetDateTime startedAt;
  OffsetDateTime finishedAt;

  public DateRangeDto getDateRange() {
    return new DateRangeDto(getRangeFrom(), getRangeTo());
  }
}
