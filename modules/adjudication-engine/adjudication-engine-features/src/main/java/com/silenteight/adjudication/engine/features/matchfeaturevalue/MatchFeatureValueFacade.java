package com.silenteight.adjudication.engine.features.matchfeaturevalue;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.features.matchfeaturevalue.dto.MatchFeatureValue;

import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class MatchFeatureValueFacade {

  private final CreateMatchFeatureValuesUseCase createMatchFeatureValuesUseCase;

  public void createMatchFeatureValues(Collection<MatchFeatureValue> valueDtos) {
    createMatchFeatureValuesUseCase.createMatchFeatureValues(valueDtos);
  }
}
