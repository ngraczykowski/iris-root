package com.silenteight.serp.common.testing;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.serp.common.support.spring.ModularConfigLocationEnvironmentPostProcessor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;

import java.util.Map;

import static org.springframework.boot.context.config.ConfigFileApplicationListener.CONFIG_LOCATION_PROPERTY;

/**
 * Injects {@link org.springframework.core.env.PropertySource} that sets
 * {@code classpath:/testing-config/application.yml} as additional configuration file pulled in
 * for testing purposes.
 */
@Slf4j
public class TestingConfigLocationEnvironmentPostProcessor
    implements EnvironmentPostProcessor, Ordered {

  private static final String PROPERTY_SOURCE_NAME = "testingConfig";

  private static final int DEFAULT_ORDER =
      ModularConfigLocationEnvironmentPostProcessor.DEFAULT_ORDER + 1;

  @Setter
  private int order = DEFAULT_ORDER;

  @Override
  public int getOrder() {
    return order;
  }

  @Override
  public void postProcessEnvironment(ConfigurableEnvironment environment,
                                     SpringApplication application) {
    MutablePropertySources propertySources = environment.getPropertySources();

    if (propertySources.contains(PROPERTY_SOURCE_NAME))
      return;

    StringBuilder configLocationBuilder = new StringBuilder(
        environment.getProperty(CONFIG_LOCATION_PROPERTY, ""));

    if (configLocationBuilder.length() > 0)
      configLocationBuilder.append(',');

    configLocationBuilder.append("classpath:/testing-config/application-testing.yml");

    MapPropertySource testingConfigLocation = new MapPropertySource(PROPERTY_SOURCE_NAME, Map.of(
        CONFIG_LOCATION_PROPERTY,
        configLocationBuilder.toString()));

    propertySources.addFirst(testingConfigLocation);
  }
}
