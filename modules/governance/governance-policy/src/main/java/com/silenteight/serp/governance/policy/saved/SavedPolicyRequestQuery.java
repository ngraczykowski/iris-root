package com.silenteight.serp.governance.policy.saved;

import com.silenteight.serp.governance.policy.domain.dto.PolicyDto;

import java.util.Collection;

public interface SavedPolicyRequestQuery {

  Collection<PolicyDto> listSaved();
}
