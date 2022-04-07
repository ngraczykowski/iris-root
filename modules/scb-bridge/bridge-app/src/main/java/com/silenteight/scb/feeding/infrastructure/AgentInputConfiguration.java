package com.silenteight.scb.feeding.infrastructure;

import com.silenteight.scb.feeding.domain.featureinput.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AgentInputConfiguration {

  @Bean
  NameFeatureInputFactory nameFeatureInputFactory() {
    return new NameFeatureInputFactory();
  }

  @Bean
  DateFeatureInputFactory dateFeatureInputFactory() {
    return new DateFeatureInputFactory();
  }

  @Bean
  GenderFeatureInputFactory genderFeatureInputFactory() {
    return new GenderFeatureInputFactory();
  }

  @Bean
  NationalIdFeatureInputFactory nationalIdFeatureInputFactory() {
    return new NationalIdFeatureInputFactory();
  }

  @Bean
  DocumentNumberFeatureInputFactory otherDocumentFeatureInputFactory() {
    return new DocumentNumberFeatureInputFactory();
  }

  @Bean
  PassportNumberDocumentFeatureInputFactory passportNumberDocumentFeatureInputFactory() {
    return new PassportNumberDocumentFeatureInputFactory();
  }

  @Bean
  NationalityFeatureInputFactory nationalityFeatureInputFactory() {
    return new NationalityFeatureInputFactory();
  }

  @Bean
  CountryFeatureInputFactory countryAgentInputCreator() {
    return new CountryFeatureInputFactory();
  }

  // TODO: uncomment once uds-lib will provide api for registering companyName feature inputs
  //  @Bean
  //  CompanyNameFeatureInputFactory companyNameFeatureInputFactory() {
  //    return new CompanyNameFeatureInputFactory();
  //  }
}
