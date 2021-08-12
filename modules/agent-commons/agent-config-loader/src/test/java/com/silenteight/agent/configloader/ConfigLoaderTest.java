package com.silenteight.agent.configloader;

import lombok.Data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ConfigLoaderTest {

  private AgentConfigsLoader<TestConfigProperties> configLoader;

  @BeforeEach
  void setUp() {
    configLoader =
        new AgentConfigsLoader<>("test-app", "foo.bar", TestConfigProperties.class);
  }

  @Test
  void shouldLoadConfigFromFile() throws IOException {
    // when
    AgentConfigs<TestConfigProperties> agentConfigs = configLoader.load();

    // then
    TestConfigProperties configProperties = agentConfigs.agentConfigs().get("test-app-config");
    assertThat(configProperties).isNotNull();

    // and
    Duration evaluationTimeout = configProperties.getEvaluationTimeout();
    assertThat(evaluationTimeout).isEqualTo(Duration.parse("PT1S"));

    // and
    List<String> priorities = configProperties.getDecisions().getPriority();
    assertThat(priorities).containsExactly(
        "EXACT_MATCH", "STRONG_MATCH", "MATCH", "INCONCLUSIVE", "WEAK_MATCH", "NO_MATCH",
        "HQ_NO_MATCH", "NO_DATA");
  }

  @Data
  static class TestConfigProperties {

    @NotNull
    private Duration evaluationTimeout;

    @Valid
    private DecisionProperties decisions = new DecisionProperties();

    @Data
    static class DecisionProperties {

      @Valid
      private List<String> priority = new ArrayList<>();
    }
  }
}
