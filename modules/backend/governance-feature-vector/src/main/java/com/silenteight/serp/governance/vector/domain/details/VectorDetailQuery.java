package com.silenteight.serp.governance.vector.domain.details;

import com.silenteight.serp.governance.vector.domain.dto.FeatureVectorWithUsageDto;

public interface VectorDetailQuery {

  FeatureVectorWithUsageDto findByFvSignature(String fvSignature);
}
