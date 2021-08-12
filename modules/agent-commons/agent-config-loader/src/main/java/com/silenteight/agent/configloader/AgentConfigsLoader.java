package com.silenteight.agent.configloader;

import lombok.RequiredArgsConstructor;

import org.apache.commons.io.FilenameUtils;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.Files.isHidden;
import static java.nio.file.Files.isRegularFile;

@RequiredArgsConstructor
public class AgentConfigsLoader<PropertiesTypeT> {

  private final String applicationName;
  private final String prefix;
  private final Class<PropertiesTypeT> propertiesType;

  public AgentConfigs<PropertiesTypeT> load() throws IOException {
    Path applicationConfigs = ConfigsPathFinder.findDirectory(applicationName);

    AgentConfigs<PropertiesTypeT> agentConfigs = new AgentConfigs<>();

    try (DirectoryStream<Path> configFiles = Files.newDirectoryStream(applicationConfigs)) {
      for (Path configFile : configFiles) {
        if (isRegularFile(configFile) && !isHidden(configFile)) {
          String agentName = getAgentName(configFile);
          PropertiesTypeT agentProperties = ConfigParser.parse(configFile, prefix, propertiesType);
          agentConfigs.put(agentName, agentProperties);
        }
      }
    }
    return agentConfigs;
  }

  private static String getAgentName(Path configFile) {
    String fileName = String.valueOf(configFile.getFileName());
    return FilenameUtils.removeExtension(fileName);
  }
}
