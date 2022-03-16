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
  NationalIdDocumentAgentInputCreator nationalIdDocumentAgentInputCreator() {
    return new NationalIdDocumentAgentInputCreator();
  }

  @Bean
  NationalityCountryAgentInputCreator nationalityCountryAgentInputCreator() {
    return new NationalityCountryAgentInputCreator();
  }

  @Bean
  OtherDocumentAgentInputCreator otherDocumentAgentInputCreator() {
    return new OtherDocumentAgentInputCreator();
  }

  @Bean
  PassportNumberDocumentAgentInputCreator passportNumberDocumentAgentInputCreator() {
    return new PassportNumberDocumentAgentInputCreator();
  }

  @Bean
  ResidencyCountryAgentInputCreator residencyCountryAgentInputCreator() {
    return new ResidencyCountryAgentInputCreator();
  }
}
