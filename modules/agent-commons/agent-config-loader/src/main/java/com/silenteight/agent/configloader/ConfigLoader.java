package com.silenteight.agent.configloader;

import lombok.RequiredArgsConstructor;

import org.apache.commons.io.FilenameUtils;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

@RequiredArgsConstructor
public class ConfigLoader<PropertiesTypeT> {

  private final ConfigsPathFinder configsPathFinder;
  private final String prefix;
  private final Class<PropertiesTypeT> propertiesType;

  public ConfigLoader(
      String applicationName, String prefix, Class<PropertiesTypeT> propertiesType) {
    this(new ConfigsPathFinder(applicationName), prefix, propertiesType);
  }

  public AgentConfigs<PropertiesTypeT> load() throws IOException {
    Path applicationConfigs = configsPathFinder.find();

    AgentConfigs<PropertiesTypeT> agentConfigs = new AgentConfigs<>();
    try (DirectoryStream<Path> configFiles = Files.newDirectoryStream(applicationConfigs)) {
      for (Path configFile : configFiles) {
        String agentName = getAgentName(configFile);
        PropertiesTypeT agentProperties = loadProperties(configFile);
        agentConfigs.put(agentName, agentProperties);
      }
    }
    return agentConfigs;
  }

  private static String getAgentName(Path configFile) {
    String fileName = String.valueOf(configFile.getFileName());
    return FilenameUtils.removeExtension(fileName);
  }

  private PropertiesTypeT loadProperties(Path configFile) {
    String resourceLocation = configFile.toUri().toString();
    SpringConfigurationPropertiesLoader propertiesLoader =
        new SpringConfigurationPropertiesLoader(resourceLocation);
    return propertiesLoader.load(prefix, propertiesType);
  }

}
