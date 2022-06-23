package com.silenteight.serp.governance.vector.list;

import com.silenteight.serp.governance.vector.domain.dto.FeatureVectorWithUsageDto;

import java.util.stream.Stream;

public interface FeatureVectorUsageQuery {

  Stream<FeatureVectorWithUsageDto> getAllWithUsage();
}
