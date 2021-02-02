package com.silenteight.serp.governance.policy.featurevector;

import com.silenteight.serp.governance.policy.featurevector.dto.FeatureVectorWithUsageDto;

import java.util.stream.Stream;

public interface FeatureVectorUsageQuery {

  Stream<FeatureVectorWithUsageDto> getAllWithUsage();
}
