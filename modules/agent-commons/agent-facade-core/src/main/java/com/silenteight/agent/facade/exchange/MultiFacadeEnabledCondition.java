package com.silenteight.agent.facade.exchange;

import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

class MultiFacadeEnabledCondition extends AtLeastOneFacadeEnabledCondition {

  private static final String MULTI_FACADE_PROPERTY_PREFIX = "facade.amqp.queueDefinitions";

  @Override
  public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
    var atLeastOneFacadeEnabled = super.matches(context, metadata);
    return atLeastOneFacadeEnabled && hasMultiFacadeProperty(context.getEnvironment());
  }

  private static boolean hasMultiFacadeProperty(Environment environment) {
    return getAllProperties(environment)
        .anyMatch(property -> property.startsWith(MULTI_FACADE_PROPERTY_PREFIX));
  }
}
