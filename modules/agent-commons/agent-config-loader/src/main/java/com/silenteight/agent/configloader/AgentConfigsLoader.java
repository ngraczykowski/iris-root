package com.silenteight.agent.configloader;

import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.Files.isHidden;
import static java.nio.file.Files.isRegularFile;
import static org.apache.commons.io.FilenameUtils.removeExtension;

@RequiredArgsConstructor
public class AgentConfigsLoader<PropertiesTypeT> {

  private final String configDir;
  private final String prefix;
  private final Class<PropertiesTypeT> propertiesType;

  public AgentConfigs<PropertiesTypeT> load() throws IOException {
    Path configsRootPath = ConfigsPathFinder.findDirectory(configDir);

    AgentConfigs<PropertiesTypeT> agentConfigs = new AgentConfigs<>();

    try (var configFiles = Files.walk(configsRootPath)) {
      for (var iterator = configFiles.iterator(); iterator.hasNext(); ) {
        var configFile = iterator.next();
        if (isRegularFile(configFile) && !isHidden(configFile)) {
          String agentName = getAgentName(configsRootPath, configFile);
          PropertiesTypeT agentProperties = ConfigParser.parse(configFile, prefix, propertiesType);
          agentConfigs.put(agentName, agentProperties);
        }
      }
    }

    return agentConfigs;
  }

  private static String getAgentName(Path root, Path configFile) {
    var relativePath = root.relativize(configFile);
    return removeExtension(relativePath.toString());
  }
}
