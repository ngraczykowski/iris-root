package com.silenteight.serp.governance.policy.domain;

import lombok.NoArgsConstructor;

import java.util.UUID;
import java.util.stream.Stream;

import static com.silenteight.serp.governance.policy.domain.Condition.IS;
import static com.silenteight.serp.governance.policy.domain.DomainConstants.*;
import static java.util.List.of;
import static java.util.UUID.randomUUID;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class TestFixtures {

  public static final UUID POLICY_ID = randomUUID();
  public static final UUID POLICY_ID_CLONED = randomUUID();
  public static final String POLICY_NAME = "policy_name";
  public static final String POLICY_DESCRIPTION = "Policy description";
  public static final String USER = "username";
  public static final String OTHER_USER = "username2";
  public static final String NEW_POLICY_NAME = "new name";
  public static final String NEW_DESCRIPTION = "new description name";
  public static final String FEATURE_NAME = "name";

  public static final UUID FIRST_STEP_ID = randomUUID();
  public static final UUID SECOND_STEP_ID = randomUUID();
  public static final UUID THIRD_STEP_ID = randomUUID();
  public static final UUID OTHER_STEP_ID = randomUUID();
  public static final String STEP_NAME = "step name";
  public static final String STEP_DESCRIPTION = "step description";
  public static final String OTHER_STEP_NAME = "other step name";
  public static final String OTHER_STEP_DESCRIPTION = "other step description";
  private static final String POLICY_NAME_WITH_MAX_LENGTH = "a".repeat(MAX_POLICY_NAME_LENGTH);
  private static final String POLICY_NAME_WITH_MIN_LENGTH = "a".repeat(MIN_POLICY_NAME_LENGTH);
  private static final String POLICY_NAME_THAT_EXCEEDED_MAX_LENGTH =
      "a".repeat(MAX_POLICY_NAME_LENGTH + 1);
  private static final String POLICY_NAME_SHORTER_THAN_MIN_LENGTH =
      "a".repeat(MIN_POLICY_NAME_LENGTH - 1);
  public static final String STEP_NAME_WITH_MAX_LENGTH = "a".repeat(MAX_STEP_NAME_LENGTH);
  public static final String STEP_NAME_WITH_MIN_LENGTH = "a".repeat(MIN_STEP_NAME_LENGTH);
  public static final String STEP_NAME_THAT_EXCEEDED_MAX_LENGTH =
      "a".repeat(MAX_STEP_NAME_LENGTH + 1);
  public static final String STEP_NAME_SHORTER_THAN_MIN_LENGTH =
      "a".repeat(MIN_STEP_NAME_LENGTH - 1);
  public static final String FEATURE_NAME_WITH_MAX_LENGTH =
      "a".repeat(MAX_FEATURE_NAME_LENGTH);
  public static final String FEATURE_NAME_WITH_MIN_LENGTH =
      "a".repeat(MIN_FEATURE_NAME_LENGTH);
  public static final String FEATURE_NAME_THAT_EXCEEDED_MAX_LENGTH =
      "a".repeat(MAX_FEATURE_NAME_LENGTH + 1);
  public static final String FEATURE_NAME_SHORTER_THAN_MIN_LENGTH =
      "a".repeat(MIN_FEATURE_NAME_LENGTH - 1);

  public static final MatchCondition MATCH_CONDITION = new MatchCondition("IS", IS, of("TRUE"));
  public static final FeatureLogic FEATURE_LOGIC = new FeatureLogic(1, of(MATCH_CONDITION));

  public static Stream<String> getPolicyNames() {
    return Stream.of(POLICY_NAME, POLICY_NAME_WITH_MIN_LENGTH, POLICY_NAME_WITH_MAX_LENGTH);
  }

  public static Stream<String> getIncorrectPolicyNames() {
    return Stream.of(POLICY_NAME_SHORTER_THAN_MIN_LENGTH, POLICY_NAME_THAT_EXCEEDED_MAX_LENGTH);
  }

  public static Stream<String> getFeatureNames() {
    return Stream.of(FEATURE_NAME, FEATURE_NAME_WITH_MIN_LENGTH, FEATURE_NAME_WITH_MAX_LENGTH);
  }

  public static Stream<String> getIncorrectFeatureNames() {
    return Stream.of(FEATURE_NAME_SHORTER_THAN_MIN_LENGTH, FEATURE_NAME_THAT_EXCEEDED_MAX_LENGTH);
  }

  public static Stream<String> getStepNames() {
    return Stream.of(STEP_NAME, STEP_NAME_WITH_MIN_LENGTH, STEP_NAME_WITH_MAX_LENGTH);
  }

  public static Stream<String> getIncorrectStepNames() {
    return Stream.of(STEP_NAME_SHORTER_THAN_MIN_LENGTH, STEP_NAME_THAT_EXCEEDED_MAX_LENGTH);
  }
}
