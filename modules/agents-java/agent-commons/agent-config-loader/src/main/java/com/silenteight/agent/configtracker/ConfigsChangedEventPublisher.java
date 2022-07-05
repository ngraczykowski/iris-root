package com.silenteight.agent.configtracker;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
class ConfigsChangedEventPublisher {

  private final List<ConfigsChangedListener<?>> listeners;

  @SuppressWarnings({ "rawtypes", "unchecked" })
  void publish(ConfigsChangedEvent event) {
    for (var listener : listeners) {
      if (listener.supportsConfigType(event.getPropertiesType())) {
        listener.onConfigsChange(event);
      }
    }
  }
}
