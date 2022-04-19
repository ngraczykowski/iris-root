package com.silenteight.agent.facade.exchange;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.lang.Boolean.parseBoolean;
import static java.util.regex.Pattern.compile;

class AtLeastOneFacadeEnabledCondition implements Condition {

  private static final Pattern AGENT_FACADE_ENABLING_PATTERN =
      compile("[a-zA-Z]+\\.agent\\.facade\\.enabled");

  @Override
  public boolean matches(
      ConditionContext context,
      AnnotatedTypeMetadata metadata) {
    Environment environment = context.getEnvironment();
    return getAllProperties(environment)
        .filter(property -> AGENT_FACADE_ENABLING_PATTERN.matcher(property).find())
        .anyMatch(property -> parseBoolean(environment.getProperty(property)));
  }

  private Stream<String> getAllProperties(Environment environment) {
    MutablePropertySources sources = ((AbstractEnvironment) environment).getPropertySources();

    return StreamSupport
        .stream(sources.spliterator(), false)
        .filter(ps -> ps instanceof EnumerablePropertySource)
        .map(ps -> ((EnumerablePropertySource<?>) ps).getPropertyNames())
        .flatMap(Arrays::stream)
        .distinct();
  }

}
