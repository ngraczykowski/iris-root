package com.silenteight.serp.governance.agent.domain;

import com.silenteight.serp.governance.agent.domain.dto.FeatureDto;
import com.silenteight.serp.governance.agent.domain.dto.FeaturesListDto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.silenteight.serp.governance.agent.AgentFixture.COUNTRY_AGENT;
import static com.silenteight.serp.governance.agent.AgentFixture.DOCUMENT_AGENT;
import static com.silenteight.serp.governance.agent.AgentFixture.NAME_AGENT;
import static com.silenteight.serp.governance.agent.AgentFixture.PEP_AGENT;
import static com.silenteight.serp.governance.agent.domain.file.config.AgentConfigFixture.DATE_AGENT_CONFIG_NAME;
import static com.silenteight.serp.governance.agent.domain.file.details.AgentDetailsFixture.AGENT_FEATURE_DOCUMENT_DISPLAY_NAME;
import static com.silenteight.serp.governance.agent.domain.file.details.AgentDetailsFixture.AGENT_FEATURE_NATIONALITY_DISPLAY_NAME;
import static com.silenteight.serp.governance.agent.domain.file.details.AgentDetailsFixture.AGENT_FEATURE_RESIDENCY_DISPLAY_NAME;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AgentMappingServiceTest {

  public static final int FEATURE_NATIONALITY_INDEX = 0;
  public static final int FEATURE_RESIDENCY_INDEX = 1;
  @Mock
  AgentsRegistry agentsRegistry;

  @InjectMocks
  AgentMappingService underTest;

  @BeforeEach
  void setUp() {
    underTest = new AgentsConfiguration().agentMappingService(agentsRegistry);
  }

  @Test
  void shouldReturnListMappedAgentsWithCorrectSize() {
    when(agentsRegistry.getAllAgents())
        .thenReturn(asList(PEP_AGENT, NAME_AGENT, DOCUMENT_AGENT, COUNTRY_AGENT));

    FeaturesListDto allAgents = underTest.getFeaturesListDto();

    assertThat(allAgents.getFeatures().size()).isEqualTo(5);
  }

  @Test
  void shouldReturnEmptyListWhenNoAgents() {
    when(agentsRegistry.getAllAgents()).thenReturn(emptyList());

    FeaturesListDto allAgents = underTest.getFeaturesListDto();

    assertThat(allAgents.getFeatures()).isEmpty();
  }

  @Test
  void shouldReturnSameSolutionsForSameAgentAndCorrectSNumberOfAgents() {
    // given
    when(agentsRegistry.getAllAgents()).thenReturn(of(COUNTRY_AGENT));

    // when
    FeaturesListDto allAgents = underTest.getFeaturesListDto();

    //then
    assertThat(allAgents.getFeatures().size()).isEqualTo(2);

    List<String> nationalitySolutions =
        allAgents.getFeatures().get(FEATURE_NATIONALITY_INDEX).getSolutions();

    List<String> residencySolutions =
        allAgents.getFeatures().get(FEATURE_RESIDENCY_INDEX).getSolutions();

    String featureNationalityCountryDisplayName = allAgents.getFeatures().get(0).getDisplayName();
    String featureResidencyCountryDisplayName = allAgents.getFeatures().get(1).getDisplayName();

    assertThat(featureNationalityCountryDisplayName).isEqualTo(
        AGENT_FEATURE_NATIONALITY_DISPLAY_NAME);

    assertThat(featureResidencyCountryDisplayName).isEqualTo(AGENT_FEATURE_RESIDENCY_DISPLAY_NAME);
    assertThat(nationalitySolutions).isEqualTo(residencySolutions);
  }

  @Test
  void shouldReturnListOfSolutionsWithCorrectSizeAndCorrectContent() {
    //given
    when(agentsRegistry.getAllAgents()).thenReturn(of(DOCUMENT_AGENT));

    //when
    FeaturesListDto allAgents = underTest.getFeaturesListDto();

    //then
    FeatureDto featureDto = allAgents.getFeatures().get(0);
    assertThat(featureDto.getSolutions())
        .hasSize(DOCUMENT_AGENT.getSolutions().size())
        .containsAll(DOCUMENT_AGENT.getSolutions());

    assertThat(featureDto.getAgentConfig()).isEqualTo(DATE_AGENT_CONFIG_NAME);
    assertThat(featureDto.getDisplayName()).isEqualTo(AGENT_FEATURE_DOCUMENT_DISPLAY_NAME);
  }
}
