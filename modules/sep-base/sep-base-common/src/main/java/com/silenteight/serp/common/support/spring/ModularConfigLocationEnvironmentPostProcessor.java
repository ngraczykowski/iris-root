package com.silenteight.serp.common.support.spring;

import lombok.Setter;

import com.google.common.base.Strings;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.config.ConfigFileApplicationListener;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.springframework.boot.context.config.ConfigFileApplicationListener.CONFIG_LOCATION_PROPERTY;

public class ModularConfigLocationEnvironmentPostProcessor
    implements EnvironmentPostProcessor, Ordered {

  private static final String PROPERTY_SOURCE_NAME = "modularConfig";
  private static final String HOME_PROPERTY = "serp.home";

  private static final String[] DEFAULT_CONFIG_LOCATIONS = {
      "classpath:/modular-config/application.yml",
      "classpath:/config/",
  };

  private static final String DATABASE_CONFIG_LOCATION =
      "classpath:/modular-config/application-database.yml";
  private static final String MESSAGING_CONFIG_LOCATION =
      "classpath:/modular-config/application-messaging.yml";
  private static final String BATCH_CONFIG_LOCATION =
      "classpath:/modular-config/application-batch.yml";

  public static final int DEFAULT_ORDER = ConfigFileApplicationListener.DEFAULT_ORDER - 5;

  @Setter
  private int order = DEFAULT_ORDER;

  @Override
  public int getOrder() {
    return order;
  }

  @Override
  public void postProcessEnvironment(
      ConfigurableEnvironment environment,
      SpringApplication application) {

    MutablePropertySources propertySources = environment.getPropertySources();

    if (propertySources.contains(PROPERTY_SOURCE_NAME))
      return;

    List<String> configLocations = determineConfigLocations(application.getClassLoader());

    String home = environment.getProperty(HOME_PROPERTY);
    if (!Strings.isNullOrEmpty(home)) {
      configLocations.add("file:" + home + "/conf/");
      configLocations.add("file:" + home + "/conf/serp/bootstrap.properties");
    }

    MapPropertySource modularConfigLocation = new MapPropertySource(
        PROPERTY_SOURCE_NAME, Map.of(CONFIG_LOCATION_PROPERTY, String.join(",", configLocations)));

    propertySources.addFirst(modularConfigLocation);
  }

  private static List<String> determineConfigLocations(ClassLoader classLoader) {
    ModuleActivationHelper helper = new ModuleActivationHelper(classLoader);

    List<String> configLocations = new ArrayList<>(5);

    configLocations.addAll(asList(DEFAULT_CONFIG_LOCATIONS));

    if (helper.hasDatabaseClasses())
      configLocations.add(DATABASE_CONFIG_LOCATION);

    if (helper.hasMessagingClasses())
      configLocations.add(MESSAGING_CONFIG_LOCATION);

    if (helper.hasBatchClasses())
      configLocations.add(BATCH_CONFIG_LOCATION);

    configLocations.add("classpath:/config/");

    return configLocations;
  }
}
