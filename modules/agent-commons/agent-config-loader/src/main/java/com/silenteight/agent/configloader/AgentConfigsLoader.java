package com.silenteight.agent.configloader;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Path;

import static com.silenteight.agent.configloader.ConfigParser.parse;
import static com.silenteight.agent.configloader.ConfigsPathFinder.findDirectory;
import static java.nio.file.Files.isHidden;
import static java.nio.file.Files.isRegularFile;
import static java.nio.file.Files.walk;
import static org.apache.commons.io.FilenameUtils.removeExtension;
import static org.apache.commons.io.FilenameUtils.separatorsToUnix;

@Slf4j
@RequiredArgsConstructor
public class AgentConfigsLoader<T> {

  private final String configDir;
  private final String prefix;
  private final Class<T> propertiesType;

  public AgentConfigs<T> load() throws IOException {
    return getConfigs(findDirectory(configDir));
  }

  public AgentConfigs<T> load(Path configsRootPath) throws IOException {
    Path configsPath = configsRootPath.resolve(configDir);
    return getConfigs(configsPath);
  }

  private AgentConfigs<T> getConfigs(Path configsPath) throws IOException {
    AgentConfigs<T> agentConfigs = new AgentConfigs<>();

    try (var configFiles = walk(configsPath)) {
      for (var iterator = configFiles.iterator(); iterator.hasNext(); ) {
        var configFile = iterator.next();
        if (isRegularFile(configFile) && !isHidden(configFile)) {
          String agentName = getAgentName(configsPath, configFile);
          parse(configFile, prefix, propertiesType)
              .ifPresent(properties -> {
                log.info("Successfully loaded config {} [{}] from {}.",
                    agentName, properties.getClass().getSimpleName(), configFile);
                agentConfigs.put(agentName, properties);
              });
        }
      }
    }

    return agentConfigs;
  }

  private static String getAgentName(Path root, Path configFile) {
    var relativePath = root.relativize(configFile);
    var pathWithUnixSeparators = separatorsToUnix(relativePath.toString());
    return removeExtension(pathWithUnixSeparators);
  }
}
