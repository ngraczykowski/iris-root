package com.silenteight.sens.webapp.security.acl;

import com.silenteight.sens.webapp.kernel.domain.DecisionTreeId;
import com.silenteight.sens.webapp.kernel.domain.WorkflowLevel;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.model.ObjectIdentity;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.*;

public class CompositeObjectIdentityRetrievalStrategyTest {

  private static final long DECISION_TREE_ID = 1L;
  private static final int LEVEL = 2;

  private CompositeObjectIdentityRetrievalStrategy strategy;

  @Before
  public void setUp() {
    strategy = new CompositeObjectIdentityRetrievalStrategy(
        asList(new DecisionTreeIdDomainObjectIdentityGenerator(),
               new WorkflowLevelDomainObjectIdentityGenerator()));
  }

  @Test
  public void givenDecisionTreeId_getObjectIdentity() {
    // given
    DecisionTreeId decisionTreeId = DecisionTreeId.of(DECISION_TREE_ID);

    // when
    ObjectIdentity identity = strategy.getObjectIdentity(decisionTreeId);

    // then
    assertThat(identity).isInstanceOf(ObjectIdentityImpl.class);
    assertThat(identity).isEqualTo(
        new ObjectIdentityImpl(DecisionTreeId.class.getName(), String.valueOf(DECISION_TREE_ID)));
  }

  @Test
  public void givenWorkflowLevel_getObjectIdentity() {
    // given
    WorkflowLevel workflowLevel = WorkflowLevel.of(DECISION_TREE_ID, LEVEL);

    // when
    ObjectIdentity identity = strategy.getObjectIdentity(workflowLevel);

    // then
    assertThat(identity).isInstanceOf(ObjectIdentityImpl.class);
    assertThat(identity).isEqualTo(
        new ObjectIdentityImpl(WorkflowLevel.class.getName(), DECISION_TREE_ID + "@" + LEVEL));
  }
}
