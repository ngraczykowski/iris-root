package com.silenteight.serp.governance.policy.domain;

import java.util.UUID;

import static com.silenteight.serp.governance.policy.domain.Condition.IS;
import static com.silenteight.serp.governance.policy.domain.DomainConstants.MAX_STEP_NAME_LENGTH;
import static java.util.List.of;
import static java.util.UUID.randomUUID;

public interface TestFixtures {
  UUID POLICY_ID = randomUUID();
  UUID POLICY_ID_CLONED = randomUUID();
  String POLICY_NAME = "policy_name";
  String POLICY_DESCRIPTION = "Policy description";
  String USER = "username";
  String OTHER_USER = "username2";
  String NEW_POLICY_NAME = "new name";
  String NEW_DESCRIPTION = "new description name";

  UUID FIRST_STEP_ID = randomUUID();
  UUID SECOND_STEP_ID = randomUUID();
  UUID THIRD_STEP_ID = randomUUID();
  UUID OTHER_STEP_ID = randomUUID();
  String STEP_NAME = "step name";
  String STEP_DESCRIPTION = "step description";
  String OTHER_STEP_NAME = "other step name";
  String OTHER_STEP_DESCRIPTION = "other step description";
  String STEP_NAME_WITH_MAX_LENGTH = "a".repeat(MAX_STEP_NAME_LENGTH);
  String STEP_NAME_THAT_EXCEEDED_MAX_LENGTH = "a".repeat(MAX_STEP_NAME_LENGTH + 1);

  MatchCondition MATCH_CONDITION = new MatchCondition("IS", IS, of("TRUE"));
  FeatureLogic FEATURE_LOGIC = new FeatureLogic(1, of(MATCH_CONDITION));
}
