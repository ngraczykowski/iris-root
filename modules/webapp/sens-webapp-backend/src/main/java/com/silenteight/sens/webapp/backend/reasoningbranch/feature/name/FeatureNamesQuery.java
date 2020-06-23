package com.silenteight.sens.webapp.backend.reasoningbranch.feature.name;

import java.util.List;

public interface FeatureNamesQuery {

  List<String> findFeatureNames(long featureVectorId);
}
