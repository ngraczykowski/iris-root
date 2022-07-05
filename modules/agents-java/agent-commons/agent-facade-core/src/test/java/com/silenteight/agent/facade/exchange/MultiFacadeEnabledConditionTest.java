package com.silenteight.agent.facade.exchange;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.mock.env.MockEnvironment;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class MultiFacadeEnabledConditionTest {

  @Test
  void findAndMatchPropertyForMultiFacadeEnabling() {
    MockEnvironment env = new MockEnvironment();
    env.setProperty("geo.agent.facade.enabled", "true");
    env.setProperty("facade.amqp.queueDefinitions[geo].inbound-exchange-name", "xyz");

    ConditionContext context = mock(ConditionContext.class);
    when(context.getEnvironment()).thenReturn(env);

    boolean result = new MultiFacadeEnabledCondition()
        .matches(createContext(env), null);

    assertTrue(result);
  }

  @Test
  void shouldNotMatchMultiFacade() {
    MockEnvironment env = new MockEnvironment();
    env.setProperty("geo.agent.facade.enabled", "false");
    env.setProperty("facade.amqp.queueDefinitions[geo].inbound-exchange-name", "xyz");

    boolean result = new MultiFacadeEnabledCondition()
        .matches(createContext(env), null);

    assertFalse(result);
  }

  @Test
  void shouldNotMatchMultiFacadeNoPrefix() {
    MockEnvironment env = new MockEnvironment();
    env.setProperty("geo.agent.facade.enabled", "true");

    boolean result = new MultiFacadeEnabledCondition()
        .matches(createContext(env), null);

    assertFalse(result);
  }

  private ConditionContext createContext(Environment env) {
    ConditionContext context = mock(ConditionContext.class);
    when(context.getEnvironment()).thenReturn(env);

    return context;
  }
}
