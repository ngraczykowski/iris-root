package com.silenteight.serp.governance.agent.domain.file.configuration;

import com.silenteight.serp.governance.agent.TestResourceLoader;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.ResourceLoader;
import org.yaml.snakeyaml.Yaml;

import static com.silenteight.serp.governance.agent.domain.file.configuration.AgentConfigurationDetailsFixture.AGENT_CONF_DATE_INDV_NORMAL_FILE;
import static com.silenteight.serp.governance.agent.domain.file.configuration.AgentConfigurationDetailsFixture.DATE_INDV_NORMAL_OUTPUT;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.*;

class AgentConfigurationDetailsClientTest {

  private AgentConfigurationDetailsClient underTest;

  private final Yaml yaml = new Yaml();

  @Test
  void shouldReturnConfigurationDetails() {
    //given
    underTest = new AgentConfigurationDetailsClient(
        getResourceLoader(AGENT_CONF_DATE_INDV_NORMAL_FILE),
        yaml);
    //when
    String details = underTest.getDetails(AGENT_CONF_DATE_INDV_NORMAL_FILE);
    //then
    assertThat(details).contains(DATE_INDV_NORMAL_OUTPUT);
  }

  @Test
  void shouldThrowExceptionIfConfigNotPresent() {
    String configFile = "NOT_EXISTING";
    underTest = new AgentConfigurationDetailsClient(getResourceLoader(configFile), yaml);

    assertThatThrownBy(() -> underTest.getDetails("NOT_EXISTING"))
        .isInstanceOf(UnreachableConfigurationException.class)
        .hasMessageContaining(format(
            "Agent configuration details for the following file=%s, cannot be obtained",
            configFile));
  }

  private ResourceLoader getResourceLoader(String resource) {
    return new TestResourceLoader(new ClassPathResource(resource));
  }
}
