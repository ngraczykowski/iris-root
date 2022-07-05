package com.silenteight.agent.configloader;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.nio.file.Files;
import java.nio.file.Path;

@NoArgsConstructor(access = AccessLevel.NONE)
public class ConfigsPathFinder {

  static final String AGENT_HOME = "AGENT_HOME";
  private static final String CONF_DIR = "conf";

  /**
   * Finds path to directory {AGENT_HOME}/conf/{subDir}
   */
  static Path findDirectory(String subDir) {
    Path applicationConfigs = getApplicationConfigsPath().resolve(subDir);

    if (!Files.isDirectory(applicationConfigs)) {
      throw new IllegalStateException("Cannot find configs in: " + applicationConfigs);
    }

    return applicationConfigs;
  }

  /**
   * Finds path to file {AGENT_HOME}/conf/{filePath}
   */
  public static Path findFile(String filePath) {
    return getApplicationConfigsPath().resolve(filePath);
  }

  /**
   * Finds path to file {AGENT_HOME}/conf/{subdir}/{filePath}
   */
  public static Path findFile(String subDir, String filePath) {
    return findDirectory(subDir).resolve(filePath);
  }

  /**
   * Finds path to directory {AGENT_HOME}/conf
   */
  private static Path getApplicationConfigsPath() {
    var home = new HomeDirectoryDiscoverer(AGENT_HOME);
    var applicationDir = home.discover()
        .map(Path::toString)
        .orElseThrow(() -> new IllegalStateException(
            "Config directory does not exist or " + AGENT_HOME + " not set"));

    return Path.of(applicationDir, CONF_DIR);
  }
}
