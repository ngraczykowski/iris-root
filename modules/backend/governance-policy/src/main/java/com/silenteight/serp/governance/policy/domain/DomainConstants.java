package com.silenteight.serp.governance.policy.domain;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class DomainConstants {

  public static final int MIN_FEATURE_NAME_LENGTH = 3;
  public static final int MAX_FEATURE_NAME_LENGTH = 80;
  public static final int MIN_POLICY_NAME_LENGTH = 3;
  public static final int MAX_POLICY_NAME_LENGTH = 80;
  public static final int MIN_STEP_NAME_LENGTH = 3;
  public static final int MAX_STEP_NAME_LENGTH = 150;

  public static final String POLICY_ENDPOINT_TAG = "Policy";
}
