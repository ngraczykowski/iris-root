package com.silenteight.agent.configtracker;

import lombok.RequiredArgsConstructor;

import com.silenteight.agent.configloader.AgentConfigs;
import com.silenteight.agent.configloader.AgentConfigsLoader;

import java.io.IOException;

@RequiredArgsConstructor
public class ConfigsLoader<PropertiesTypeT> {

  private final String prefix;
  private final Class<PropertiesTypeT> propertiesType;

  public AgentConfigs<PropertiesTypeT> load(String applicationName) {
    try {
      return new AgentConfigsLoader<>(applicationName, prefix, propertiesType)
          .load();
    } catch (IOException e) {
      throw new IllegalStateException("Cannot load configs for: " + applicationName, e);
    }
  }

  Class<PropertiesTypeT> getPropertiesType() {
    return propertiesType;
  }
}
