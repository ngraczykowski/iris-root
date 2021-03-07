package com.silenteight.serp.governance.policy.current;

import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.policy.solve.InUsePolicyQuery;

import java.util.Optional;

@RequiredArgsConstructor
public class CurrentPolicyProvider {

  private static final String RESOURCE_COLLECTION = "policies/";

  private final InUsePolicyQuery inUsePolicyQuery;

  public Optional<String> getCurrentPolicy() {
    return inUsePolicyQuery.getPolicyInUse()
        .map(uuid -> RESOURCE_COLLECTION + uuid);
  }
}
