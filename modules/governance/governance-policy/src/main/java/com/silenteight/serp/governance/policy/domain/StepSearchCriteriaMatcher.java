package com.silenteight.serp.governance.policy.domain;

import com.silenteight.serp.governance.policy.domain.dto.StepSearchCriteriaDto;

interface StepSearchCriteriaMatcher {

  boolean match(StepSearchCriteriaDto criteria);
}
