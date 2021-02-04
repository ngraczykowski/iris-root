package com.silenteight.serp.governance.policy.startup;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.policy.solve.InUsePolicyLoader;

import javax.annotation.PostConstruct;

@RequiredArgsConstructor
class DefaultPolicyLoader {

  @NonNull
  private final InUsePolicyLoader policyLoader;

  @PostConstruct
  void init() {
    policyLoader.loadInUsePolicy();
  }
}
