package com.silenteight.agent.configloader;

import lombok.Data;
import lombok.SneakyThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ConfigLoaderTest {

  @SneakyThrows
  private AgentConfigs<TestConfigProperties> loadConfigs(String configDir) {
    return new AgentConfigsLoader<>(configDir, "foo.bar", TestConfigProperties.class).load();
  }

  @Test
  void shouldLoadConfigFromFile() {
    // when
    AgentConfigs<TestConfigProperties> agentConfigs = loadConfigs("test-app");

    // then
    TestConfigProperties configProperties = agentConfigs.getRequired("test-app-config");
    assertThat(configProperties).isNotNull();

    // and
    assertThat(configProperties.getName()).isEqualTo("test");

    // and
    Duration evaluationTimeout = configProperties.getEvaluationTimeout();
    assertThat(evaluationTimeout).isEqualTo(Duration.parse("PT1S"));

    // and
    List<String> priorities = configProperties.getDecisions().getPriority();
    assertThat(priorities).containsExactly(
        "EXACT_MATCH", "STRONG_MATCH", "MATCH", "INCONCLUSIVE", "WEAK_MATCH", "NO_MATCH",
        "HQ_NO_MATCH", "NO_DATA");
  }

  @Test
  void shouldLoadNestedConfigs() {
    // when
    AgentConfigs<TestConfigProperties> configs = loadConfigs("test-app");

    // then
    assertThat(configs.size()).isEqualTo(3);
    assertThat(configs.getRequired("test-app-config").getName()).isEqualTo("test");
    assertThat(configs.getRequired("nested/nested-app-config").getName()).isEqualTo("nested");
    assertThat(configs.getRequired("nested/nested/nested-nested-app-config").getName())
        .isEqualTo("nested-nested");
  }

  @Test
  void shouldChangeAgentInstanceName_whenLoadConfigsFromNestedDirectory() {  // when
    AgentConfigs<TestConfigProperties> configs = loadConfigs("test-app/nested");

    // then
    assertThat(configs.size()).isEqualTo(2);
    assertThat(configs.getRequired("nested-app-config").getName()).isEqualTo("nested");
    assertThat(configs.getRequired("nested/nested-nested-app-config").getName())
        .isEqualTo("nested-nested");
  }

  @Data
  static class TestConfigProperties {

    @NotNull
    private Duration evaluationTimeout;

    @NotNull
    private String name;

    @Valid
    private DecisionProperties decisions = new DecisionProperties();

    @Data
    static class DecisionProperties {

      @Valid
      private List<String> priority = new ArrayList<>();
    }
  }
}
