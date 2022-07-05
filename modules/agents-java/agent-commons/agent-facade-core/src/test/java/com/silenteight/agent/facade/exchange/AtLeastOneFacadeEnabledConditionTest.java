package com.silenteight.agent.facade.exchange;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.mock.env.MockEnvironment;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class AtLeastOneFacadeEnabledConditionTest {

  @Test
  void findAndMatchPropertyForFacadeEnabling() {
    MockEnvironment env = new MockEnvironment();
    env.setProperty("geo.agent.facade.enabled", "true");

    ConditionContext context = mock(ConditionContext.class);
    when(context.getEnvironment()).thenReturn(env);

    boolean result = new AtLeastOneFacadeEnabledCondition()
        .matches(createContext(env), null);

    assertTrue(result);
  }

  @Test
  void shouldNotMatchPropertyUnrelatedToAgentFacades() {
    MockEnvironment env = new MockEnvironment();
    env.setProperty("logging.level.org", "ERROR");

    boolean result = new AtLeastOneFacadeEnabledCondition()
        .matches(createContext(env), null);

    assertFalse(result);
  }

  private ConditionContext createContext(Environment env) {
    ConditionContext context = mock(ConditionContext.class);
    when(context.getEnvironment()).thenReturn(env);

    return context;
  }
}
