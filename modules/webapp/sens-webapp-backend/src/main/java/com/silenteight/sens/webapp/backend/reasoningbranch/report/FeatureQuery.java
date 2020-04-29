package com.silenteight.sens.webapp.backend.reasoningbranch.report;

import java.util.List;

public interface FeatureQuery {

  List<String> findFeaturesNames(long featureVectorId);
}
