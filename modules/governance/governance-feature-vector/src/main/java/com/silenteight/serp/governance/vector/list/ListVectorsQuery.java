package com.silenteight.serp.governance.vector.list;

import com.silenteight.serp.governance.common.web.rest.Paging;
import com.silenteight.serp.governance.vector.domain.dto.FeatureVectorsDto;

public interface ListVectorsQuery {

  FeatureVectorsDto list(Paging paging);

  int count();
}
