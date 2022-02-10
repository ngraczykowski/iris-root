package com.silenteight.sep.base.common.support.spring;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.base.common.logging.LogMarkers;

import org.springframework.boot.context.event.ApplicationFailedEvent;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.boot.context.event.SpringApplicationEvent;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.*;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.joining;

@Slf4j
public class EnvironmentLoggingApplicationListener
    implements ApplicationListener<SpringApplicationEvent> {

  private static final int MAX_VALUE_LENGTH = 120;
  private static final String SHOW_ENV_PROPERTY = "serp.show-env";
  private static final String SHOW_ENV_TRUNCATE_PROPERTY = "serp.show-env.truncate";
  private final SensitivePropertiesMatcher
      sensitivePropertiesMatcher = new SensitivePropertiesMatcher();

  private final Object syncRoot = new Object() {};

  private Environment environment = null;

  @Override
  public void onApplicationEvent(SpringApplicationEvent event) {
    if (event instanceof ApplicationStartedEvent) {
      ApplicationStartedEvent startedEvent = (ApplicationStartedEvent) event;
      onEnvironmentPrepared(startedEvent.getApplicationContext());
    }

    if (event instanceof ApplicationFailedEvent) {
      ApplicationFailedEvent failedEvent = (ApplicationFailedEvent) event;
      onEnvironmentPrepared(failedEvent.getApplicationContext());
    }
  }

  private void onEnvironmentPrepared(ConfigurableApplicationContext context) {
    synchronized (syncRoot) {
      // NOTE(ahaczewski): Log once only.
      if (environment != null)
        return;

      environment = context.getEnvironment();

      // NOTE(ahaczewski): Do not log in bootstrap environment.
      if (context.getEnvironment().getPropertySources().contains("bootstrap"))
        return;

      if (isDebugEnabled())
        logEnvironment(context);
    }
  }

  private boolean isDebugEnabled() {
    boolean debug = environment.getProperty("debug") != null;
    boolean showEnvSet = environment.getProperty(SHOW_ENV_PROPERTY) != null;
    boolean showEnv = environment.getProperty(SHOW_ENV_PROPERTY, Boolean.class, Boolean.TRUE);

    if (!showEnvSet)
      return debug;

    return showEnv;
  }

  private void logEnvironment(ApplicationContext context) {
    log.info(LogMarkers.INTERNAL, "======== Environment and configuration ========");
    logVersion(context);
    logActiveProfiles();

    if (environment instanceof AbstractEnvironment) {
      MutablePropertySources sources = ((AbstractEnvironment) environment).getPropertySources();
      logDetails(sources);
    }
  }

  private void logVersion(ApplicationContext context) {
    getBean(context, BuildProperties.class).ifPresent(
        bean -> log.info(LogMarkers.INTERNAL, "Application version: {}", bean.getVersion()));
    getBean(context, GitProperties.class).ifPresent(bean -> {
      log.info(LogMarkers.INTERNAL, "Git commit: {}", bean.getCommitId());
      log.info(LogMarkers.INTERNAL, "Git commit time: {}", bean.getCommitTime());
    });
  }

  private static <T> Optional<T> getBean(ApplicationContext context, Class<T> clazz) {
    return Stream
        .of(context.getBeanNamesForType(clazz))
        .findFirst()
        .map(name -> context.getBean(name, clazz));
  }

  private void logActiveProfiles() {
    log.info(LogMarkers.INTERNAL, "Active profiles: {}", (Object) environment.getActiveProfiles());
  }

  private void logDetails(MutablePropertySources sources) {
    logPropertySources(sources);
    logPropertiesValues(sources);
  }

  private static void logPropertySources(MutablePropertySources sources) {
    String propertySources = StreamSupport
        .stream(sources.spliterator(), false)
        .map(PropertySource::getName)
        .collect(joining(
            "\n",
            "\n==================== Property sources ===================\n",
            "\n================ End of property sources ================\n"));

    log.info(LogMarkers.INTERNAL, propertySources);
  }

  private void logPropertiesValues(MutablePropertySources sources) {
    String propertyValues = StreamSupport
        .stream(sources.spliterator(), false)
        .filter(ps -> ps instanceof EnumerablePropertySource)
        .map(ps -> ((EnumerablePropertySource<?>) ps).getPropertyNames())
        .flatMap(Arrays::stream)
        .distinct()
        .sorted()
        .map(prop -> prop + ": " + getPropertyValue(prop))
        .collect(joining(
            "\n",
            "\n==================== Property values ====================\n",
            "\n================ End of property values =================\n"));

    log.info(LogMarkers.INTERNAL, propertyValues);
  }

  private String getPropertyValue(String prop) {
    try {
      if (isPropertySensitive(prop))
        return "<********>";

      return ofNullable(environment.getProperty(prop))
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
    int valueLength = value.length();

    String sanitized = value;

    if (shouldTruncate() && valueLength > MAX_VALUE_LENGTH)
      sanitized = value.substring(0, MAX_VALUE_LENGTH) + "<...>";

    return sanitized.replace("\n", "<LF>").replace("\r", "<CR>");
  }

  private boolean shouldTruncate() {
    return environment.getProperty(SHOW_ENV_TRUNCATE_PROPERTY) != null;
  }
}
