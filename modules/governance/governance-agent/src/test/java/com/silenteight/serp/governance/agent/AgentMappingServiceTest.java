package com.silenteight.serp.governance.agent;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.silenteight.serp.governance.agent.AgentFixture.COUNTRY_AGENT;
import static com.silenteight.serp.governance.agent.AgentFixture.DOB_AGENT;
import static com.silenteight.serp.governance.agent.AgentFixture.DOCUMENT_AGENT;
import static com.silenteight.serp.governance.agent.AgentFixture.NAME_AGENT;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AgentMappingServiceTest {

  public static final int FEATURE_NATIONALITY_INDEX = 0;
  public static final int FEATURE_RESIDENCY_INDEX = 1;
  @Mock
  AgentsRegistry agentsRegistry;

  @InjectMocks
  AgentMappingService agentMappingService;

  @BeforeEach
  void setUp() {
    agentMappingService = new AgentsConfiguration().agentMappingService(agentsRegistry);
  }

  @Test
  void shouldReturnListMappedAgentsWithCorrectSize() {
    when(agentsRegistry.getAllAgents())
        .thenReturn(asList(DOB_AGENT, NAME_AGENT, DOCUMENT_AGENT, COUNTRY_AGENT));

    FeaturesListDto allAgents = agentMappingService.getFeaturesListDto();

    assertThat(allAgents.getFeatures().size()).isEqualTo(5);
  }

  @Test
  void shouldReturnEmptyListWhenNoAgents() {
    when(agentsRegistry.getAllAgents()).thenReturn(emptyList());

    FeaturesListDto allAgents = agentMappingService.getFeaturesListDto();

    assertThat(allAgents.getFeatures()).isEmpty();
  }

  @Test
  void shouldReturnSameSolutionsForSameAgentAndCorrectSNumberOfAgents() {
    // given
    when(agentsRegistry.getAllAgents()).thenReturn(asList(COUNTRY_AGENT));

    // when
    FeaturesListDto allAgents = agentMappingService.getFeaturesListDto();

    //then
    assertThat(allAgents.getFeatures().size()).isEqualTo(2);

    List<String> nationalitySolutions =
        allAgents.getFeatures().get(FEATURE_NATIONALITY_INDEX).getSolutions();
    List<String> residencySolutions =
        allAgents.getFeatures().get(FEATURE_RESIDENCY_INDEX).getSolutions();

    assertThat(nationalitySolutions).isEqualTo(residencySolutions);
  }

  @Test
  void shouldReturnListOfSolutionsWithCorrectSizeAndCorrectContent() {
    //given
    when(agentsRegistry.getAllAgents()).thenReturn(asList(DOCUMENT_AGENT));

    //when
    FeaturesListDto allAgents = agentMappingService.getFeaturesListDto();

    //then
    assertThat(allAgents.getFeatures().get(0).getSolutions())
        .hasSize(DOCUMENT_AGENT.getSolutions().size())
        .containsAll(DOCUMENT_AGENT.getSolutions());
  }
}
