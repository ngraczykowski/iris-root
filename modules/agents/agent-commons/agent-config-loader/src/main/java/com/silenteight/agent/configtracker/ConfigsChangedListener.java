package com.silenteight.agent.configtracker;

public interface ConfigsChangedListener<PropertiesTypeT> {

  boolean supportsConfigType(Class<?> configType);

  void onConfigsChange(ConfigsChangedEvent<PropertiesTypeT> event);
}
