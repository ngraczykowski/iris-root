package com.silenteight.payments.bridge.firco.decision.decisionmapping;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.firco.decision.decisionmode.DecisionMode;
import com.silenteight.payments.bridge.firco.decision.decisionmode.DecisionModeResolver;
import com.silenteight.payments.bridge.firco.decision.statemapping.StateMappingStrategy;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(StateMappingStrategySelectorProperties.class)
// HACK(ahaczewski): Force StateMappingStrategyConfiguration to initialize before this class.
@DependsOn("stateMappingStrategyConfiguration")
class StateMappingStrategySelectorConfiguration {

  private final DecisionModeResolver decisionModeResolver;
  private final StateMappingStrategySelectorProperties properties;
  private final ObjectProvider<StateMappingStrategy> stateMappingStrategies;

  @Bean
  StateMappingStrategySelector stateMappingStrategySelector() {
    Map<DecisionMode, StateMappingStrategy> mappingStrategies = new LinkedHashMap<>();

    properties.getDecisionModeMappingStrategies().forEach((decisionMode, strategyName) ->
        mappingStrategies.put(decisionMode, findStrategy(strategyName))
    );

    return new StateMappingStrategySelector(decisionModeResolver, mappingStrategies);
  }

  private StateMappingStrategy findStrategy(String name) {
    return stateMappingStrategies
        .stream()
        .filter(strategy -> strategy.getName().equals(name))
        .findAny()
        .orElseThrow();
  }
}
