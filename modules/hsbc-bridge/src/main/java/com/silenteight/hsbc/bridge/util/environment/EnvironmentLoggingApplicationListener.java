package com.silenteight.hsbc.bridge.util.environment;

import lombok.extern.slf4j.Slf4j;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.boot.context.event.ApplicationFailedEvent;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.boot.context.event.SpringApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.*;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
class EnvironmentLoggingApplicationListener implements ApplicationListener<SpringApplicationEvent> {

  private static final int MAX_VALUE_LENGTH = 120;
  private static final String SHOW_ENV_PROPERTY = "serp.show-env";
  private static final String SHOW_ENV_TRUNCATE_PROPERTY = "serp.show-env.truncate";
  private static final Marker INTERNAL = MarkerFactory.getDetachedMarker("INTERNAL");
  private final SensitivePropertiesMatcher sensitivePropertiesMatcher = new SensitivePropertiesMatcher();

  private final Object syncRoot = new Object() {};

  private Environment environment = null;

  @Override
  public void onApplicationEvent(@NotNull SpringApplicationEvent event) {
    if (event instanceof ApplicationStartedEvent) {
      var startedEvent = (ApplicationStartedEvent) event;
      onEnvironmentPrepared(startedEvent.getApplicationContext().getEnvironment());
    }

    if (event instanceof ApplicationFailedEvent) {
      var failedEvent = (ApplicationFailedEvent) event;
      onEnvironmentPrepared(failedEvent.getApplicationContext().getEnvironment());
    }
  }

  private void onEnvironmentPrepared(ConfigurableEnvironment configurableEnvironment) {
    synchronized (syncRoot) {
      // NOTE(ahaczewski): Log once only.
      if (environment != null) {
        return;
      }

      environment = configurableEnvironment;

      // NOTE(ahaczewski): Do not log in bootstrap environment.
      if (configurableEnvironment.getPropertySources().contains("bootstrap")) {
        return;
      }

      if (isDebugEnabled()) {
        logEnvironment();
      }
    }
  }

  private boolean isDebugEnabled() {
    var debug = environment.getProperty("debug") != null;
    var showEnvSet = environment.getProperty(SHOW_ENV_PROPERTY) != null;
    var showEnv = environment.getProperty(SHOW_ENV_PROPERTY, Boolean.class, Boolean.TRUE);

    if (!showEnvSet) {
      return debug;
    }

    return showEnv;
  }

  private void logEnvironment() {
    log.info(INTERNAL, "======== Environment and configuration ========");

    logActiveProfiles();

    if (environment instanceof AbstractEnvironment) {
      var sources = ((AbstractEnvironment) environment).getPropertySources();
      logDetails(sources);
    }
  }

  private void logActiveProfiles() {
    log.info(INTERNAL, "Active profiles: {}", (Object) environment.getActiveProfiles());
  }

  private void logDetails(MutablePropertySources sources) {
    logPropertySources(sources);
    logPropertiesValues(sources);
  }

  private static void logPropertySources(MutablePropertySources sources) {
    var propertySources = StreamSupport
        .stream(sources.spliterator(), false)
        .map(PropertySource::getName)
        .collect(Collectors.joining(
            "\n",
            "\n==================== Property sources ===================\n",
            "\n================ End of property sources ================\n"));

    log.info(INTERNAL, propertySources);
  }

  private void logPropertiesValues(MutablePropertySources sources) {
    var propertyValues = StreamSupport
        .stream(sources.spliterator(), false)
        .filter(EnumerablePropertySource.class::isInstance)
        .map(ps -> ((EnumerablePropertySource<?>) ps).getPropertyNames())
        .flatMap(Arrays::stream)
        .distinct()
        .sorted()
        .map(prop -> prop + ": " + getPropertyValue(prop))
        .collect(Collectors.joining(
            "\n",
            "\n==================== Property values ====================\n",
            "\n================ End of property values =================\n"));

    log.info(INTERNAL, propertyValues);
  }

  private String getPropertyValue(String prop) {
    try {
      if (isPropertySensitive(prop)) {
        return "<********>";
      }
      return Optional.ofNullable(environment.getProperty(prop))
          .map(this::sanitizePropertyValue)
          .orElse("<null>");
    } catch (Exception e) {
      log.debug("Cannot evaluate property value: key={}", prop);
      return "<error>";
    }
  }

  private boolean isPropertySensitive(String prop) {
    return sensitivePropertiesMatcher.matches(prop);
  }

  private String sanitizePropertyValue(String value) {
    var valueLength = value.length();

    var sanitized = value;

    if (shouldTruncate() && valueLength > MAX_VALUE_LENGTH) {
      sanitized = value.substring(0, MAX_VALUE_LENGTH) + "<...>";
    }

    return sanitized.replace("\n", "<LF>").replace("\r", "<CR>");
  }

  private boolean shouldTruncate() {
    return environment.getProperty(SHOW_ENV_TRUNCATE_PROPERTY) != null;
  }
}
