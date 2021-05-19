package com.silenteight.serp.governance.agent.domain.file.configuration;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.base.common.support.jackson.JsonConversionHelper;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.core.io.ResourceLoader;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;

import static java.lang.String.format;

@RequiredArgsConstructor
@Slf4j
public class AgentConfigurationDetailsClient {

  @NonNull
  private final ResourceLoader resourceLoader;

  @NonNull
  private final Yaml yaml;

  private static final String RESOURCE_CLASSPATH_FORMAT = "classpath:%s";

  public String getDetails(String configurationFile) {
    String configuration;
    try {
      configuration = parseConfigurationsDetails(getInputStream(configurationFile));
    } catch (IOException e) {
      log.error("Agent configuration details failed, could not load file: " + configurationFile, e);
      throw new UnreachableConfigurationException(configurationFile);
    }
    return configuration;
  }

  private String parseConfigurationsDetails(InputStream inputStream)
      throws JsonProcessingException {

    Object configuration = yaml.load(inputStream);
    return JsonConversionHelper.INSTANCE.objectMapper().writeValueAsString(configuration);
  }

  private InputStream getInputStream(String configurationFile) throws IOException {
    return resourceLoader
        .getResource(format(RESOURCE_CLASSPATH_FORMAT, configurationFile))
        .getInputStream();
  }
}
