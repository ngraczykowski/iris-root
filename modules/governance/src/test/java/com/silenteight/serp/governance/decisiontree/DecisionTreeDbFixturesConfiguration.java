package com.silenteight.serp.governance.decisiontree;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = { DecisionTreeModule.class })
public class DecisionTreeDbFixturesConfiguration {

  @Bean
  DecisionTreeDbFixtureService decisionTreeDbFixtureService(
      DecisionTreeRepository decisionTreeRepository) {
    return new DecisionTreeDbFixtureService(decisionTreeRepository);
  }
}
