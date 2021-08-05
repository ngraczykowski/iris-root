package com.silenteight.agent.configloader;

import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigsPathFinder {

  static final String AGENT_HOME = "AGENT_HOME";
  private static final String CONF_DIR = "conf";

  private final String applicationName;

  public ConfigsPathFinder(String applicationName) {
    this.applicationName = applicationName;
  }

  public Path find() {
    Path applicationConfigs = getApplicationConfigsPath();

    if (!Files.isDirectory(applicationConfigs)) {
      throw new IllegalStateException("Cannot find configs in: " + applicationConfigs);
    }

    return applicationConfigs;
  }

  public Path findFile(String subDir, String filePath) {
    return find().resolve(subDir).resolve(filePath);
  }

  private Path getApplicationConfigsPath() {
    var home = new HomeDirectoryDiscoverer(AGENT_HOME);
    var applicationDir = home.discover()
        .map(Path::toString)
        .orElseThrow(() -> new IllegalStateException(
            "Config directory does not exist or " + AGENT_HOME + " not set"));

    return Path.of(applicationDir, CONF_DIR, applicationName);
  }
}
