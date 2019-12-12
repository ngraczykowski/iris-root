package com.silenteight.sens.webapp.common.app;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.springframework.boot.Banner.Mode;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.util.*;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Stream.of;

/**
 * Provides sensible defaults for all SENS applications.
 */
public class SensWebAppApplicationBuilder {

  private final List<String> configNames = new ArrayList<>();
  private final List<String> configLocations = new ArrayList<>();
  private final List<String> additionalConfigLocations = new ArrayList<>();
  private final Set<String> additionalProfiles = new LinkedHashSet<>();

  private final Map<String, String> systemProperties = new LinkedHashMap<>();
  private final Map<String, Object> defaultProperties = new LinkedHashMap<>();
  private final Map<String, String> systemFromEnv = new LinkedHashMap<>();

  SensWebAppApplicationBuilder() {
    configLocation(
        "file:${sens.config.dir:${config.dir:${sens.home:${user.dir}}/conf}/sens}/",
        "file:${config.dir:${sens.home:${user.dir}}/conf}/override.properties");

    properties("spring.aop.proxy-target-class=true");

    propertiesFromEnv(
        "sens.config.dir=SENS_CONFIG_DIR",
        "sens.home=SENS_HOME",
        "config.dir=CONFIG_DIR");
  }

  public SensWebAppApplicationBuilder configLocation(String... locations) {
    setArrayValues(configLocations, locations);
    return this;
  }

  public SensWebAppApplicationBuilder configName(String... names) {
    setArrayValues(configNames, names);
    return this;
  }

  private void setArrayValues(List<String> arrayToUpdate, String[] values) {
    arrayToUpdate.clear();
    if (values.length > 0)
      arrayToUpdate.addAll(asList(values));
  }

  public SensWebAppApplicationBuilder profiles(String... profiles) {
    additionalProfiles.addAll(asList(profiles));
    return this;
  }

  public SensWebAppApplicationBuilder bootstrapProperties(String... properties) {
    systemProperties.putAll(getMapFromProperties(properties));
    return this;
  }

  public SensWebAppApplicationBuilder properties(String... properties) {
    defaultProperties.putAll(getMapFromProperties(properties));
    return this;
  }

  public SensWebAppApplicationBuilder propertiesFromEnv(String... envToSystem) {
    systemFromEnv.putAll(getMapFromProperties(envToSystem));
    return this;
  }

  private Map<String, String> getMapFromProperties(String[] properties) {
    Map<String, String> map = new LinkedHashMap<>();

    for (String property : properties) {
      String[] tokens = StringUtils.split(property, "=:", 2);
      if (tokens == null)
        continue;
      String key = tokens[0];
      String value = tokens.length > 1 ? tokens[1] : "";
      map.put(key, value);
    }

    return map;
  }

  SpringApplicationBuilder createSpringBuilder(Class<?>... sources) {
    SpringApplicationBuilder applicationBuilder = new SpringApplicationBuilder(sources);

    return applyProperties(applicationBuilder);
  }

  private SpringApplicationBuilder applyProperties(SpringApplicationBuilder builder) {
    applyBasicProperties(builder);
    applyAdditionalProperties(builder);

    return builder;
  }

  private void applyBasicProperties(SpringApplicationBuilder builder) {
    builder.bannerMode(Mode.OFF);

    if (SystemUtils.IS_OS_WINDOWS)
      builder.profiles("windows");

    if (SystemUtils.IS_OS_LINUX)
      builder.profiles("linux");

    builder.profiles(additionalProfiles.toArray(new String[0]));

    builder.web(WebApplicationType.SERVLET);
  }

  private void applyAdditionalProperties(SpringApplicationBuilder builder) {
    builder.properties(defaultProperties);
  }

  Runnable getSystemPropertiesSetter() {
    return this::setupSystemProperties;
  }

  private void setupSystemProperties() {
    applyConfigProperties();

    systemFromEnv.forEach(this::copyPropertyFromEnv);
    systemProperties.forEach(this::setSystemPropertyDefault);
  }

  private void applyConfigProperties() {
    String configs = makeCommaSeparatedPropertyValue(additionalConfigLocations, configLocations);
    setSystemPropertyDefault("spring.config.name", makeCommaSeparatedPropertyValue(configNames));
    setSystemPropertyDefault("spring.config.additional-location", configs);
  }

  private void setSystemPropertyDefault(String name, String value) {
    if (System.getProperty(name) == null)
      System.setProperty(name, value);
  }

  private void copyPropertyFromEnv(String systemProperty, String env) {
    String envValue = StringUtils.trimToNull(System.getenv(env));

    if (StringUtils.isNotBlank(envValue))
      setSystemPropertyDefault(systemProperty, envValue);
  }

  @SafeVarargs
  private final String makeCommaSeparatedPropertyValue(List<String>... lists) {
    if (lists.length > 0)
      return of(lists).flatMap(Collection::stream).collect(joining(","));
    else
      return "";
  }
}
