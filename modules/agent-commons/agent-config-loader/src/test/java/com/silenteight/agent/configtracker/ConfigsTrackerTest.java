package com.silenteight.agent.configtracker;

import lombok.Data;

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
    // given
    var loaders = List.of(
        new ConfigsLoader<>("first.cfg", FirstConfigProperties.class),
        new ConfigsLoader<>("second.cfg", SecondConfigProperties.class));
    var firstListener = spy(new FirstConfigListener());
    var secondListener = spy(new SecondConfigListener());
    var listeners = List.of(firstListener, secondListener);
    var publisher = spy(new ConfigsChangedEventPublisher(listeners));
    var tracker = new ContextRefreshedConfigsChangeTracker(publisher, loaders);

    // when
    tracker.onRefreshContext(new ContextRefreshedEvent(applicationContext));

    // then
    verify(publisher, times(2)).publish(any());
    verify(firstListener).onConfigsChange(any());
    verify(secondListener).onConfigsChange(any());
  }

  static class FirstConfigListener implements ConfigsChangedListener<FirstConfigProperties> {

    @Override
    public boolean supportsConfigType(Class<?> configType) {
      return FirstConfigProperties.class.isAssignableFrom(configType);
    }

    @Override
    public void onConfigsChange(
        ConfigsChangedEvent<FirstConfigProperties> event) {
      assertThat(event.getConfigs()).isNotNull();
      assertThat(event.getConfigs().agentConfigs()).containsOnlyKeys("confs");
      FirstConfigProperties props = event.getConfigs().agentConfigs().get("confs");
      assertThat(props.getYearExclusions()).containsExactly("9999");
      assertThat(props.getMonthDayExclusions()).containsExactly("01-01");
    }
  }

  static class SecondConfigListener implements ConfigsChangedListener<SecondConfigProperties> {

    @Override
    public boolean supportsConfigType(Class<?> configType) {
      return SecondConfigProperties.class.isAssignableFrom(configType);
    }

    @Override
    public void onConfigsChange(
        ConfigsChangedEvent<SecondConfigProperties> event) {
      assertThat(event.getConfigs()).isNotNull();
      assertThat(event.getConfigs().agentConfigs()).containsOnlyKeys("confs");
      SecondConfigProperties props = event.getConfigs().agentConfigs().get("confs");
      assertThat(props.getResultPriority()).containsExactly("EXACT", "NEAR", "OUT_OF_RANGE");
    }
  }


  @Data
  static class FirstConfigProperties {

    private List<String> yearExclusions = new ArrayList<>();
    private List<String> monthDayExclusions = new ArrayList<>();
  }

  @Data
  static class SecondConfigProperties {

    private List<String> resultPriority = new ArrayList<>();
  }
}
