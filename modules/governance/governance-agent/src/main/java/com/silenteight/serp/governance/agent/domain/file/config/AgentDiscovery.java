package com.silenteight.serp.governance.agent.domain.file.config;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import javax.annotation.PostConstruct;

import static java.util.Set.of;
import static java.util.stream.Collectors.toUnmodifiableSet;

@RequiredArgsConstructor
@Slf4j
public class AgentDiscovery {

  @NonNull
  private final String source;

  @NonNull
  private final ObjectMapper objectMapper;

  @NonNull
  private final ResourceLoader resourceLoader;

  @NonNull
  private final AtomicReference<Set<AgentConfigDto>> agentConfigsRef = new AtomicReference<>(of());

  public Set<AgentConfigDto> getAgentConfigs() {
    return agentConfigsRef.get();
  }

  @PostConstruct
  void loadAgents() {
    try (InputStream inputStream = resourceLoader.getResource(source).getInputStream()) {
      agentConfigsRef.set(parseAgentConfigs(inputStream));
      log.debug("Agent list loaded from file: "
          + " source=" + source
          + " entriesCount=" + agentConfigsRef.get().size());
    } catch (Exception e) {
      log.error("Agent discovery failed, could not load file: " + source, e);
      throw new AgentDiscoveryException(e);
    }
  }

  private Set<AgentConfigDto> parseAgentConfigs(InputStream inputStream) throws IOException {
    AgentConfigsWrapperJson agentConfigsWrapper = objectMapper
        .readValue(inputStream, AgentConfigsWrapperJson.class);

    return agentConfigsWrapper.getAgentConfigs().stream()
        .map(AgentConfigJson::toDto)
        .collect(toUnmodifiableSet());
  }
}
