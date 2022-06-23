package com.silenteight.serp.governance.qa.sampling.job;

import com.silenteight.serp.governance.qa.sampling.domain.dto.AlertSamplingDto;
import com.silenteight.serp.governance.qa.sampling.domain.dto.DateRangeDto;

import java.util.List;

public interface AlertSamplingByState {

  List<AlertSamplingDto> listFinished(DateRangeDto dateRangeDto);
}
