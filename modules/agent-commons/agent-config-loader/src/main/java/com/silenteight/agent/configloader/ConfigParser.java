package com.silenteight.agent.configloader;

import lombok.NoArgsConstructor;

import java.nio.file.Path;

@NoArgsConstructor
public class ConfigParser {

  /**
   * Loads YAML from given path and parse it to properties type
   *
   * @param configFile
   *     Path to single file
   * @param prefix
   *     Namespace in Yaml file that will be used for deserialization
   * @param propertiesType
   *     Class of property that Yaml file will be deserialized to
   *
   * @return Serialized object of class <code>propertiesType</code>
   */

  public static <PropertiesTypeT> PropertiesTypeT parse(
      Path configFile,
      String prefix,
      Class<PropertiesTypeT> propertiesType) {

    String resourceLocation = configFile.toUri().toString();

    SpringConfigurationPropertiesLoader propertiesLoader =
        new SpringConfigurationPropertiesLoader(resourceLocation);

    return propertiesLoader.load(prefix, propertiesType);
  }
}
