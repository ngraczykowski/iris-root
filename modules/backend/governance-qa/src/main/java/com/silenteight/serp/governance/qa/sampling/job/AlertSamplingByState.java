package com.silenteight.serp.governance.qa.sampling.job;

import com.silenteight.serp.governance.qa.sampling.domain.dto.AlertSamplingDto;
import com.silenteight.serp.governance.qa.sampling.domain.dto.DateRangeDto;

import java.util.List;
import javax.validation.Valid;

public interface AlertSamplingByState {

  List<AlertSamplingDto> listFinished(@Valid DateRangeDto dateRangeDto);
}
