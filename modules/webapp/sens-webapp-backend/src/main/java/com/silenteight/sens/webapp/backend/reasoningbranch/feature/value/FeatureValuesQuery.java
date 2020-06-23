package com.silenteight.sens.webapp.backend.reasoningbranch.feature.value;

import java.util.List;

public interface FeatureValuesQuery {

  List<String> findFeatureValues(long decisionTreeId, long featureVectorId);
}
