package com.silenteight.agent.configloader;

import java.util.HashMap;
import java.util.Map;

public class AgentConfigs<PropertiesTypeT> {

  private final Map<String, PropertiesTypeT> agentNameToConfig = new HashMap<>();

  void put(String agentName, PropertiesTypeT config) {
    this.agentNameToConfig.put(agentName, config);
  }

  public Map<String, PropertiesTypeT> agentConfigs() {
    return new HashMap<>(agentNameToConfig);
  }

}
