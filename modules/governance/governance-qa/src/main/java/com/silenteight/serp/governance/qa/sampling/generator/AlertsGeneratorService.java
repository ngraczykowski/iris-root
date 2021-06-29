package com.silenteight.serp.governance.qa.sampling.generator;

import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.qa.sampling.domain.dto.DateRangeDto;

import java.util.List;
import javax.validation.Valid;

@RequiredArgsConstructor
public class AlertsGeneratorService {

  private final List<String> groupingFields;

  public void generateAlerts(@Valid DateRangeDto dateRangeDto) {
    // TODO(kdzieciol): WEB-1263
  }
}
