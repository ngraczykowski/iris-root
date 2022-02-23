package com.silenteight.serp.governance.policy.domain;

import java.util.UUID;
import java.util.stream.Stream;

import static com.silenteight.serp.governance.policy.domain.DomainConstants.MAX_POLICY_NAME_LENGTH;
import static com.silenteight.serp.governance.policy.domain.DomainConstants.MIN_POLICY_NAME_LENGTH;
import static java.util.UUID.fromString;

public final class SharedTestFixtures {

  private SharedTestFixtures() {

  }

  public static final String USER = "username";

  public static final String POLICY_NAME = "policy_name";
  public static final UUID POLICY_ID = fromString("7b0a69c9-24a2-4a3e-b64c-ccd073743416");
  public static final String POLICY_NAME_WITH_MAX_LENGTH = "a".repeat(MAX_POLICY_NAME_LENGTH);
  private static final String POLICY_NAME_WITH_MIN_LENGTH = "a".repeat(MIN_POLICY_NAME_LENGTH);
  public static final String POLICY_NAME_THAT_EXCEEDED_MAX_LENGTH =
      "a".repeat(MAX_POLICY_NAME_LENGTH + 1);
  private static final String POLICY_NAME_SHORTER_THAN_MIN_LENGTH =
      "a".repeat(MIN_POLICY_NAME_LENGTH - 1);

  public static Stream<String> getPolicyNames() {
    return Stream.of(POLICY_NAME, POLICY_NAME_WITH_MIN_LENGTH, POLICY_NAME_WITH_MAX_LENGTH);
  }

  public static Stream<String> getIncorrectPolicyNames() {
    return Stream.of(POLICY_NAME_SHORTER_THAN_MIN_LENGTH, POLICY_NAME_THAT_EXCEEDED_MAX_LENGTH);
  }
}
