package com.silenteight.scb.feeding.infrastructure;

import com.silenteight.scb.feeding.domain.agentinput.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AgentInputConfiguration {

  @Bean
  NameAgentInputCreator nameAgentInputCreator() {
    return new NameAgentInputCreator();
  }

  @Bean
  DateAgentInputCreator dateAgentInputCreator() {
    return new DateAgentInputCreator();
  }

  @Bean
  GenderAgentInputCreator genderAgentInputCreator() {
    return new GenderAgentInputCreator();
  }

  @Bean
  NationalIdAgentInputCreator nationalIdDocumentAgentInputCreator() {
    return new NationalIdAgentInputCreator();
  }

  @Bean
  DocumentNumberAgentInputCreator otherDocumentAgentInputCreator() {
    return new DocumentNumberAgentInputCreator();
  }

  @Bean
  PassportNumberDocumentAgentInputCreator passportNumberDocumentAgentInputCreator() {
    return new PassportNumberDocumentAgentInputCreator();
  }

  @Bean
  NationalityAgentInputCreator nationalityAgentInputCreator() {
    return new NationalityAgentInputCreator();
  }

  @Bean
  CountryAgentInputCreator countryAgentInputCreator() {
    return new CountryAgentInputCreator();
  }

  // TODO: uncomment once uds-lib will provide api for registering companyName feature inputs
  //  @Bean
  //  CompanyNameAgentInputCreator companyNameAgentInputCreator() {
  //    return new CompanyNameAgentInputCreator();
  //  }
}
