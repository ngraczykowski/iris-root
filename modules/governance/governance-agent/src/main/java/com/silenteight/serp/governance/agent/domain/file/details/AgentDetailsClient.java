package com.silenteight.serp.governance.agent.domain.file.details;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.serp.governance.agent.domain.file.config.AgentConfigDto;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;

import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
@Slf4j
public class AgentDetailsClient {

  @NonNull
  private final String source;

  @NonNull
  private final ObjectMapper objectMapper;

  @NonNull
  private final ResourceLoader resourceLoader;

  @NonNull
  private final ConcurrentHashMap<String, AgentDetailJson> agentDetails =
      new ConcurrentHashMap<>();

  public AgentDetailDto getAgentDetailsForAgentConfig(AgentConfigDto agentConfigDto) {
    return ofNullable(agentDetails.get(agentConfigDto.getName()))
        .orElseThrow(() -> new UnreachableAgentException(agentConfigDto))
        .toDto(agentConfigDto.getName());
  }

  @PostConstruct
  void init() {
    try (InputStream inputStream = resourceLoader.getResource(source).getInputStream()) {
      agentDetails.putAll(parseAgentDetails(inputStream));
      log.debug("Agent configuration loaded from file: "
          + " source=" + source
          + " entriesCount=" + agentDetails.size());
    } catch (Exception e) {
      log.error("Loading configuration for Agents failed, could not load file: " + source, e);
    }
  }

  private Map<String, AgentDetailJson> parseAgentDetails(InputStream inputStream)
      throws IOException {

    TypeReference<HashMap<String, AgentDetailJson>> agentDetailsWrapperType
        = new TypeReference<>() {};
    return objectMapper.readValue(inputStream, agentDetailsWrapperType);
  }
}
