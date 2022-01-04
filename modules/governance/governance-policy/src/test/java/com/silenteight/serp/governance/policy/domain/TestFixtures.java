package com.silenteight.serp.governance.policy.domain;

import java.util.UUID;

import static com.silenteight.serp.governance.policy.domain.Condition.IS;
import static java.util.List.of;
import static java.util.UUID.randomUUID;

public class TestFixtures {
  public static final UUID POLICY_ID = randomUUID();
  public static final UUID POLICY_ID_CLONED = randomUUID();
  public static final String POLICY_NAME = "policy_name";
  public static final String USER = "username";
  public static final String OTHER_USER = "username2";
  public static final String NEW_POLICY_NAME = "new name";
  public static final String NEW_DESCRIPTION = "new description name";

  public static final UUID FIRST_STEP_ID = randomUUID();
  public static final UUID SECOND_STEP_ID = randomUUID();
  public static final UUID THIRD_STEP_ID = randomUUID();
  public static final UUID OTHER_STEP_ID = randomUUID();
  public static final String STEP_NAME = "step name";
  public static final String STEP_DESCRIPTION = "step description";
  public static final String OTHER_STEP_NAME = "other step name";
  public static final String OTHER_STEP_DESCRIPTION = "other step description";

  public static final MatchCondition MATCH_CONDITION = new MatchCondition("IS", IS, of("TRUE"));
  public static final FeatureLogic FEATURE_LOGIC = new FeatureLogic(1, of(MATCH_CONDITION));
}
