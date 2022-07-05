package com.silenteight.agent.configloader;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.agents.logging.AgentLogger;

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
    Path configsRootPath = findDirectory(configDir);

    AgentConfigs<T> agentConfigs = new AgentConfigs<>();

    try (var configFiles = walk(configsRootPath)) {
      for (var iterator = configFiles.iterator(); iterator.hasNext(); ) {
        var configFile = iterator.next();
        if (isRegularFile(configFile) && !isHidden(configFile)) {
          String agentName = getAgentName(configsRootPath, configFile);
          parse(configFile, prefix, propertiesType)
              .ifPresent(properties -> {
                AgentLogger.info(log, "Successfully loaded config {} [{}] from {}.",
                    () -> agentName,
                    () -> properties.getClass().getSimpleName(),
                    () -> configFile);
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
