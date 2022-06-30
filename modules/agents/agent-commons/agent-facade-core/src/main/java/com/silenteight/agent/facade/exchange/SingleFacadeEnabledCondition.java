package com.silenteight.agent.facade.exchange;

import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

class SingleFacadeEnabledCondition extends AtLeastOneFacadeEnabledCondition {

  private static final String SINGLE_FACADE_PROPERTY = "facade.amqp.inboundExchangeName";

  @Override
  public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
    var atLeastOneFacadeEnabled = super.matches(context, metadata);
    return atLeastOneFacadeEnabled && hasSingleFacadeProperty(context.getEnvironment());
  }

  private static boolean hasSingleFacadeProperty(Environment environment) {
    return environment.containsProperty(SINGLE_FACADE_PROPERTY);
  }
}
