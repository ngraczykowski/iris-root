package com.silenteight.serp.governance.qa.sampling.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.qa.sampling.domain.dto.AlertSamplingDto;
import com.silenteight.serp.governance.qa.sampling.domain.dto.DateRangeDto;
import com.silenteight.serp.governance.qa.sampling.job.AlertSamplingByState;

import java.util.List;
import javax.validation.Valid;

import static com.silenteight.serp.governance.qa.sampling.domain.JobState.FINISHED;
import static java.util.List.of;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
public class AlertSamplingByStateQuery implements AlertSamplingByState {

  @NonNull
  private final AlertSamplingRepository alertSamplingRepository;

  @Override
  public List<AlertSamplingDto> listFinished(@Valid DateRangeDto dateRangeDto) {
    return alertSamplingRepository
        .getByDateRangeAndStates(dateRangeDto.getFrom(), dateRangeDto.getTo(), of(FINISHED))
        .stream()
        .map(AlertSampling::toDto)
        .collect(toList());
  }
}
