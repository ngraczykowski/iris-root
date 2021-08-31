package com.silenteight.adjudication.engine.features.matchfeaturevalue;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.features.matchfeaturevalue.dto.MatchFeatureValue;
import com.silenteight.sep.base.aspects.metrics.Timed;

import org.springframework.stereotype.Service;

import java.util.Collection;
import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
class CreateMatchFeatureValuesUseCase {

  private final MatchFeatureValueDataAccess dataAccess;

  @Timed(value = "ae.features.use_cases", extraTags = { "package", "matchfeaturevalue" })
  @Transactional
  void createMatchFeatureValues(Collection<MatchFeatureValue> valueDtos) {
    int savedCount = dataAccess.saveAll(valueDtos);

    log.info(
        "Saved match feature values: valueCount={}, savedCount={}", valueDtos.size(), savedCount);
  }
}
