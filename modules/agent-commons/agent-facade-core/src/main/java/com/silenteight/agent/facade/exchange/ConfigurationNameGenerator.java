package com.silenteight.agent.facade.exchange;

import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import static java.lang.String.format;
import static java.util.regex.Pattern.compile;
import static java.util.stream.Collectors.toSet;

class ConfigurationNameGenerator {

  private static final Pattern VERSION_CONFIG_PATTERN = compile("versions.(.*).configs.(.*)");

  static Set<String> getConfigurationNames(List<String> features, String routingKey) {
    var matcher = VERSION_CONFIG_PATTERN.matcher(routingKey);
    if (matcher.find()) {
      var agentVersion = matcher.group(1);
      var configVersion = matcher.group(2);
      return features
          .stream()
          .map(f -> buildConfigName(f, agentVersion, configVersion))
          .collect(toSet());
    }
    return Set.of(routingKey);
  }

  private static String buildConfigName(String feature, String agentVersion, String configVersion) {
    return format("features/%s/versions/%s/configs/%s", feature, agentVersion, configVersion);
  }
}
