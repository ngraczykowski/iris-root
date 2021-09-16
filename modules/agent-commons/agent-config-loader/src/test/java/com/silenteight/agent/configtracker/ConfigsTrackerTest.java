package com.silenteight.agent.configtracker;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.List.of;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConfigsTrackerTest {

  @Mock
  private ApplicationContext applicationContext;

  @Mock
  private Environment environment;

  @BeforeEach
  void setUp() {
    when(applicationContext.getEnvironment()).thenReturn(environment);
    when(environment.getRequiredProperty("spring.application.name")).thenReturn("tracker");
  }

  @Test
  void shouldPropagateConfigsToSpecificListeners() {
    //given
    var firstListener = spy(FirstConfigListener.builder()
        .expectedAgent("confs", "confs1")
        .expectedAgent("nested/confs", "nested-confs1")
        .expectedAgent("first_confs", "first-confs")
        .build());
    var secondListener = spy(SecondConfigListener.builder()
        .expectedAgent("confs", "confs2")
        .expectedAgent("nested/confs", "nested-confs2")
        .expectedAgent("second_confs", "second-confs")
        .build());

    runTestFor(firstListener, secondListener);
  }

  @Test
  void shouldPropagateNestedConfigsToSpecificListeners() {
    //given
    when(environment.getProperty("application.config.dir")).thenReturn("tracker/nested");
    var firstListener = spy(FirstConfigListener.builder()
        .expectedAgent("confs", "nested-confs1")
        .build());
    var secondListener = spy(SecondConfigListener.builder()
        .expectedAgent("confs", "nested-confs2")
        .build());

    runTestFor(firstListener, secondListener);
  }

  private void runTestFor(FirstConfigListener firstListener, SecondConfigListener secondListener) {
    //given
    var listeners = of(firstListener, secondListener);
    var publisher = spy(new ConfigsChangedEventPublisher(listeners));
    var loaders = of(
        new ConfigsLoader<>("first.cfg", FirstConfigProperties.class),
        new ConfigsLoader<>("second.cfg", SecondConfigProperties.class));
    var tracker = new ContextRefreshedConfigsChangeTracker(publisher, loaders);

    //when
    tracker.onRefreshContext(new ContextRefreshedEvent(applicationContext));

    //then
    verify(publisher, times(2)).publish(any());
    verify(firstListener).onConfigsChange(any());
    verify(secondListener).onConfigsChange(any());
  }

  @Builder
  static class FirstConfigListener implements ConfigsChangedListener<FirstConfigProperties> {

    @Singular
    private final Map<String, String> expectedAgents;

    @Override
    public boolean supportsConfigType(Class<?> configType) {
      return FirstConfigProperties.class.isAssignableFrom(configType);
    }

    @Override
    public void onConfigsChange(
        ConfigsChangedEvent<FirstConfigProperties> event) {
      assertThat(event.getConfigs()).isNotNull();
      assertThat(event.getConfigs().agentNames())
          .containsExactlyInAnyOrderElementsOf(expectedAgents.keySet());
      expectedAgents.forEach((agentName, label) -> {
        FirstConfigProperties props = event.getConfigs().getRequired(agentName);
        assertThat(props.getLabel()).isEqualTo(label);
        assertThat(props.getYearExclusions()).containsExactly("9999");
        assertThat(props.getMonthDayExclusions()).containsExactly("01-01");
      });
    }
  }

  @Builder
  static class SecondConfigListener implements ConfigsChangedListener<SecondConfigProperties> {

    @Singular
    private final Map<String, String> expectedAgents;

    @Override
    public boolean supportsConfigType(Class<?> configType) {
      return SecondConfigProperties.class.isAssignableFrom(configType);
    }

    @Override
    public void onConfigsChange(
        ConfigsChangedEvent<SecondConfigProperties> event) {
      assertThat(event.getConfigs()).isNotNull();
      assertThat(event.getConfigs().agentNames())
          .containsExactlyInAnyOrderElementsOf(expectedAgents.keySet());
      expectedAgents.forEach((agentName, label) -> {
        SecondConfigProperties props = event.getConfigs().getRequired(agentName);
        assertThat(props.getLabel()).isEqualTo(label);
        assertThat(props.getResultPriority()).containsExactly("EXACT", "NEAR", "OUT_OF_RANGE");
      });
    }
  }


  @Data
  static class FirstConfigProperties {

    private String label;
    private List<String> yearExclusions = new ArrayList<>();
    private List<String> monthDayExclusions = new ArrayList<>();
  }

  @Data
  static class SecondConfigProperties {

    private String label;
    private List<String> resultPriority = new ArrayList<>();
  }
}
