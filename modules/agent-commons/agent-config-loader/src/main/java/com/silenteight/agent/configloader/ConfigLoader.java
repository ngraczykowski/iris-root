package com.silenteight.agent.configloader;

import org.apache.commons.io.FilenameUtils;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigLoader<PropertiesTypeT> {

  private final String prefix;
  private final Class<PropertiesTypeT> propertiesType;
  private final ConfigsPathFinder configsPathFinder;

  public ConfigLoader(
      String applicationName, String prefix, Class<PropertiesTypeT> propertiesType) {
    this.prefix = prefix;
    this.propertiesType = propertiesType;
    this.configsPathFinder = new ConfigsPathFinder(applicationName);
  }

  public ConfigLoader(
      ConfigsPathFinder configsPathFinder, String prefix, Class<PropertiesTypeT> propertiesType) {
    this.configsPathFinder = configsPathFinder;
    this.prefix = prefix;
    this.propertiesType = propertiesType;
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

  public AgentConfigs<PropertiesTypeT> load(String applicationName) {
    try {
      return getConfigLoader(applicationName).load();
    } catch (IOException e) {
      throw new IllegalStateException("Cannot load configs for: " + applicationName, e);
    }
  }

  private ConfigLoader<PropertiesTypeT> getConfigLoader(String applicationName) {
    return configsPathFinder != null
           ? new ConfigLoader<>(configsPathFinder, prefix, propertiesType)
           : this;
  }

  public Class<PropertiesTypeT> getPropertiesType() {
    return propertiesType;
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
