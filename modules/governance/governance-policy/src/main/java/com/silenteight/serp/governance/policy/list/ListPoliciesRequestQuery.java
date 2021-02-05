package com.silenteight.serp.governance.policy.list;

import com.silenteight.serp.governance.policy.domain.PolicyState;
import com.silenteight.serp.governance.policy.domain.dto.PolicyDto;

import java.util.Collection;

public interface ListPoliciesRequestQuery {

  Collection<PolicyDto> list(Collection<PolicyState> states);

  Collection<PolicyDto> listAll();
}
