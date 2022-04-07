package com.silenteight.scb.feeding.infrastructure;

import com.silenteight.scb.feeding.domain.agent.input.AgentInputFactory;
import com.silenteight.scb.feeding.domain.agent.input.feature.*;
import com.silenteight.scb.feeding.domain.category.CategoryValue;

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
  CountryFeatureFactory countryAgentInputCreator() {
    return new CountryFeatureFactory();
  }

  // TODO: uncomment once uds-lib will provide api for registering companyName feature inputs
  //  @Bean
  //  CompanyNameFeatureInputFactory companyNameFeatureInputFactory() {
  //    return new CompanyNameFeatureInputFactory();
  //  }
}
