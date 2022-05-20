package com.silenteight.adjudication.engine.features.matchfeaturevalue;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.features.matchfeaturevalue.dto.MatchFeatureValue;
import com.silenteight.sep.base.aspects.metrics.Timed;

import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class MatchFeatureValueFacade {

  private final CreateMatchFeatureValuesUseCase createMatchFeatureValuesUseCase;

  @Timed(histogram = true, percentiles = { 0.5, 0.95, 0.99 })
  public void createMatchFeatureValues(Collection<MatchFeatureValue> valueDtos) {
    createMatchFeatureValuesUseCase.createMatchFeatureValues(valueDtos);
  }
}
