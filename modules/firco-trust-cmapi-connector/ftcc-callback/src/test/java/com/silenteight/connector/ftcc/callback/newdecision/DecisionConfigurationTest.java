package com.silenteight.connector.ftcc.callback.newdecision;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.assertj.core.api.Assertions.*;

@SpringJUnitConfig
class DecisionConfigurationTest {

  @Autowired
  private ResourceLoader resourceLoader;

  @DisplayName("Pass configuration should read 3 transitions")
  @Test
  void testProperConfiguration() throws Exception {
    var factory = factory("classpath:decision/decision.csv");

    var decisionConfigurationHolder = factory.getObject();
    assertThat(decisionConfigurationHolder.getDecisionTransitions()).hasSize(3);
    assertThat(decisionConfigurationHolder.getDecisionTransitions())
        .contains(new DecisionTransition("NEW", "ACTION_FALSE_POSITIVE", "Level 2-, FALSE1"));
  }

  @DisplayName("Pass configuration with no Data should throw Exception")
  @Test
  void testNoDataConfiguration() {
    var factory = factory("classpath:decision/decision_no_data.csv");
    assertThatIllegalStateException().isThrownBy(factory::getObject);
  }

  @DisplayName("Pass resource location to not existing file should throw Exception")
  @Test
  void testFileNotExistConfiguration() {
    var factory = factory("classpath:decision/decision_file_not_exist.csv");
    assertThatIOException().isThrownBy(factory::getObject);
  }

  @DisplayName("Pass resource with wrong header name should throw RuntimeException")
  @Test
  void testHeaderNames() {
    var factory = factory("classpath:decision/decision_wrong_columns.csv");
    assertThatExceptionOfType(RuntimeException.class).isThrownBy(factory::getObject);
  }

  @DisplayName("Pass resource with random Header order should bind data properly")
  @Test
  void testFileWrongColumns() throws Exception {
    var factory = factory("classpath:decision/decision_column_order.csv");
    var decisionConfigurationHolder = factory.getObject();
    assertThat(decisionConfigurationHolder.getDecisionTransitions()).hasSize(3);
    assertThat(decisionConfigurationHolder.getDecisionTransitions())
        .contains(new DecisionTransition("NEW", "ACTION_FALSE_POSITIVE", "Level 2-, FALSE1"));
  }

  @NotNull
  private DecisionConfigurationHandlerBeanFactory factory(String location) {
    var decisionConfiguration = new DecisionConfiguration();
    var properties = new DecisionConfigurationProperties();
    properties.setResourceLocation(location);
    var factory = decisionConfiguration.decisionConfigurationHandlerBeanFactory(properties);
    factory.setResourceLoader(this.resourceLoader);
    return factory;
  }
}
