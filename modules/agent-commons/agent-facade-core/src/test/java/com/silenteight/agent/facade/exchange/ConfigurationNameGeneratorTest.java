package com.silenteight.agent.facade.exchange;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class ConfigurationNameGeneratorTest {

  @Test
  void shouldGenerateConfigName() {
    var features = List.of("featureTest");
    var routingKey = "agents.geo.versions.XYZ.configs.ABC";

    var configNames = ConfigurationNameGenerator.getConfigurationNames(features, routingKey);
    assertThat(configNames).containsOnly("features/featureTest/versions/XYZ/configs/ABC");
  }

  @Test
  void shouldGenerateConfigNames() {
    var features = List.of("featureOne", "featureTwo");
    var routingKey = "agents.geo.versions.XYZ.configs.ABC";

    var configNames = ConfigurationNameGenerator.getConfigurationNames(features, routingKey);
    assertThat(configNames).containsExactly(
        "features/featureOne/versions/XYZ/configs/ABC",
        "features/featureTwo/versions/XYZ/configs/ABC");
  }

  @Test
  void shouldGenerateConfigNameWithRoutingKeyValue() {
    var features = List.of("featureTest");
    var routingKey = "InvalidRoutingKetFormat";

    var configNames = ConfigurationNameGenerator.getConfigurationNames(features, routingKey);
    assertThat(configNames).containsOnly("InvalidRoutingKetFormat");
  }

}