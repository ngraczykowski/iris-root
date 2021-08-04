package com.silenteight.agent.configtracker;

import lombok.RequiredArgsConstructor;

import com.silenteight.agent.configloader.AgentConfigs;
import com.silenteight.agent.configloader.ConfigLoader;
import com.silenteight.agent.configloader.ConfigsPathFinder;

import java.io.IOException;

@RequiredArgsConstructor
public class ConfigsLoader<PropertiesTypeT> {

  private final String prefix;
  private final Class<PropertiesTypeT> propertiesType;
  private final ConfigsPathFinder configsPathFinder;

  public ConfigsLoader(String prefix, Class<PropertiesTypeT> propertiesType) {
    this(prefix, propertiesType, null);
  }

  public AgentConfigs<PropertiesTypeT> load(String applicationName) {
    try {
      return getConfigLoader(applicationName).load();
    } catch (IOException e) {
      throw new IllegalStateException("Cannot load configs for: " + applicationName, e);
    }
  }

  private ConfigLoader<PropertiesTypeT> getConfigLoader(String applicationName) {
    return configsPathFinder != null
           ? new ConfigLoader<>(configsPathFinder, prefix, propertiesType)
           : new ConfigLoader<>(applicationName, prefix, propertiesType);
  }

  Class<PropertiesTypeT> getPropertiesType() {
    return propertiesType;
  }
}
