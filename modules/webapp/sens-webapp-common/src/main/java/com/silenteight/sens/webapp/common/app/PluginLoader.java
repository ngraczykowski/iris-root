package com.silenteight.sens.webapp.common.app;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

public enum PluginLoader {

  INSTANCE;

  private final ServiceLoader<Plugin> serviceLoader = ServiceLoader.load(Plugin.class);
  private List<Plugin> loadedPlugins;

  public Iterable<Plugin> loadPlugins() {

    synchronized (serviceLoader) {
      if (loadedPlugins == null) {
        loadedPlugins = new ArrayList<>();
        serviceLoader.iterator().forEachRemaining(loadedPlugins::add);
      }

      return loadedPlugins;
    }
  }

  public void reload() {
    loadedPlugins = null;
    serviceLoader.reload();
  }
}
