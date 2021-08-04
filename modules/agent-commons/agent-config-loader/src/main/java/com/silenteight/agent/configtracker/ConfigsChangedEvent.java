package com.silenteight.agent.configtracker;

import lombok.Value;

import com.silenteight.agent.configloader.AgentConfigs;

@Value
public class ConfigsChangedEvent<PropertiesTypeT> {
  Class<PropertiesTypeT> propertiesType;
  AgentConfigs<PropertiesTypeT> configs;
}
