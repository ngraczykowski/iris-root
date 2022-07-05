package com.silenteight.agent.configtracker;

import lombok.RequiredArgsConstructor;

import com.silenteight.agent.configloader.AgentConfigs;
import com.silenteight.agent.configloader.AgentConfigsLoader;

import java.io.IOException;

@RequiredArgsConstructor
public class ConfigsLoader<T> {

  private final String prefix;
  private final Class<T> propertiesType;

  public AgentConfigs<T> load(String configDir) {
    try {
      return new AgentConfigsLoader<>(configDir, prefix, propertiesType).load();
    } catch (IOException e) {
      throw new IllegalStateException("Cannot load configs from: " + configDir, e);
    }
  }

  Class<T> getPropertiesType() {
    return propertiesType;
  }
}
