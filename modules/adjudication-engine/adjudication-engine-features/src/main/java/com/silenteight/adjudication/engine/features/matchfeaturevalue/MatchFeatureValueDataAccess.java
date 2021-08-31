package com.silenteight.adjudication.engine.features.matchfeaturevalue;

import com.silenteight.adjudication.engine.features.matchfeaturevalue.dto.MatchFeatureValue;

public interface MatchFeatureValueDataAccess {

  int saveAll(Iterable<MatchFeatureValue> featureValues);
}
