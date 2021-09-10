package com.silenteight.agent.configloader;

import java.util.*;
import java.util.Map.Entry;
import java.util.function.BiConsumer;

public class AgentConfigs<T> {

  private final Map<String, T> agentNameToConfig = new HashMap<>();

  protected void put(String agentName, T config) {
    agentNameToConfig.put(agentName, config);
  }

  public T remove(String key) {
    return agentNameToConfig.remove(key);
  }

  public Set<Entry<String, T>> entrySet() {
    return agentNameToConfig.entrySet();
  }

  public T getRequired(String agentName) {
    return agentNameToConfig.get(agentName);
  }

  public Optional<T> get(String agentName) {
    return Optional.ofNullable(agentNameToConfig.get(agentName));
  }

  public Set<String> agentNames() {
    return agentNameToConfig.keySet();
  }

  public Collection<T> values() {
    return agentNameToConfig.values();
  }

  public int size() {
    return agentNameToConfig.size();
  }

  public void forEach(BiConsumer<String, T> action) {
    agentNameToConfig.forEach(action);
  }

  /**
   * @return agentConfigs map
   *
   * @deprecated Use <code>AgentConfigs</code> API methods instead
   */
  @Deprecated(forRemoval = true)
  public Map<String, T> agentConfigs() {
    return new HashMap<>(agentNameToConfig);
  }
}
