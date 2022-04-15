package com.silenteight.serp.governance.agent.domain;

import com.silenteight.serp.governance.agent.domain.file.configuration.AgentDetailsConfigurationDto;
import com.silenteight.serp.governance.agent.domain.file.details.NonResolvableResourceException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.*;

class AgentDetailsResolverTest {

  private static final String AGENT_NAME_DOCUMENT = "agents/documentAgent/versions/2.0.0/config/1";
  private static final String AGENT_NAME_NAME = "agents/nameAgent/versions/1.0.0/config/1";
  private static final String AGENT_ID = "a9fbb313-b9f8-4792-99ad-6fdd74b9e629";

  @Test
  void shouldResolveAgentName() {
    Assertions.assertEquals("nameAgent", AgentDetailsResolver.resolveAgentName(
        AGENT_NAME_NAME));
    Assertions.assertEquals("documentAgent", AgentDetailsResolver.resolveAgentName(
        AGENT_NAME_DOCUMENT));
  }

  @Test
  void shouldThrowExceptionWhenAgentNameMalformed() {
    String malformedAgentName = "malformedAgentName";
    assertThatThrownBy(() -> AgentDetailsResolver.resolveAgentName(malformedAgentName))
        .isInstanceOf(NonResolvableResourceException.class)
        .hasMessage(format("Malformed agent name=%s", malformedAgentName));
  }

  @Test
  void shouldResolveAgentVersion() {
    Assertions.assertEquals("1.0.0", AgentDetailsResolver.resolveAgentVersion(
        AGENT_NAME_NAME));
    Assertions.assertEquals("2.0.0", AgentDetailsResolver.resolveAgentVersion(
        AGENT_NAME_DOCUMENT));
  }

  @Test
  void shouldResolveName() {
    Assertions.assertEquals(format("agents/%s", AGENT_ID), AgentDetailsResolver.resolveName(
        AGENT_ID));
  }

  @Test
  void shouldResolveConfigurations() {
    //given
    String dateEntityStrict = "date-entity-strict";
    String dateEntityNormal = "date-entity-normal";
    List<AgentDetailsConfigurationDto> configurations = List.of(
        getAgentDetailsConfigurationDto(dateEntityStrict, "file1.yml"),
        getAgentDetailsConfigurationDto(dateEntityNormal, "file2.yml")
    );
    //when
    List<String> actualConfigurations = AgentDetailsResolver.resolveConfigurations(configurations);
    //then
    assertThat(actualConfigurations).containsExactlyInAnyOrder(dateEntityNormal, dateEntityStrict);
  }

  private AgentDetailsConfigurationDto getAgentDetailsConfigurationDto(
      String name,
      String configFile) {

    return AgentDetailsConfigurationDto
        .builder()
        .name(name)
        .configFile(configFile)
        .build();
  }
}
