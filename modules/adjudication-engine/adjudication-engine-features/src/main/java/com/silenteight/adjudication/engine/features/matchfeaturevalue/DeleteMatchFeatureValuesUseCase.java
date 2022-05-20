package com.silenteight.adjudication.engine.features.matchfeaturevalue;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.base.aspects.metrics.Timed;

import org.springframework.stereotype.Service;

import java.util.Collection;
import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
public class DeleteMatchFeatureValuesUseCase {

  private final MatchFeatureValueDataAccess dataAccess;

  @Timed(value = "ae.features.use_cases", extraTags = {
      "package", "matchfeaturevalue" }, histogram = true,
      percentiles = { 0.5, 0.95, 0.99 })
  @Transactional
  public int deleteMatchFeatureValues(Collection<String> features) {
    return dataAccess.delete(features);
  }
}
