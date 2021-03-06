/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.feeding.infrastructure;

import com.silenteight.iris.bridge.scb.feeding.domain.agent.input.AgentInputFactory;
import com.silenteight.iris.bridge.scb.feeding.domain.agent.input.feature.*;
import com.silenteight.iris.bridge.scb.feeding.domain.category.CategoryValue;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class AgentInputConfiguration {

  @Bean
  AgentInputFactory agentInputFactory(
      List<FeatureFactory> featureInputFactories,
      List<CategoryValue> categoryValues) {
    return new AgentInputFactory(featureInputFactories, categoryValues);
  }

  @Bean
  NameFeatureFactory nameFeatureInputFactory() {
    return new NameFeatureFactory();
  }

  @Bean
  DateFeatureFactory dateFeatureInputFactory() {
    return new DateFeatureFactory();
  }

  @Bean
  GenderFeatureFactory genderFeatureInputFactory() {
    return new GenderFeatureFactory();
  }

  @Bean
  NationalIdFeatureFactory nationalIdFeatureInputFactory() {
    return new NationalIdFeatureFactory();
  }

  @Bean
  DocumentNumberFeatureFactory otherDocumentFeatureInputFactory() {
    return new DocumentNumberFeatureFactory();
  }

  @Bean
  PassportNumberDocumentFeatureFactory passportNumberDocumentFeatureInputFactory() {
    return new PassportNumberDocumentFeatureFactory();
  }

  @Bean
  NationalityFeatureFactory nationalityFeatureInputFactory() {
    return new NationalityFeatureFactory();
  }

  @Bean
  ResidencyFeatureFactory residencyAgentInputCreator() {
    return new ResidencyFeatureFactory();
  }

  @Bean
  CompanyNameFeatureFactory companyNameFeatureFactory() {
    return new CompanyNameFeatureFactory();
  }
}
