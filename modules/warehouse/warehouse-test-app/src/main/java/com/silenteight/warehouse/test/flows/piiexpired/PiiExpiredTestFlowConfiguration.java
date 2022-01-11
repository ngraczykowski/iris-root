package com.silenteight.warehouse.test.flows.piiexpired;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.test.client.gateway.PersonalInformationExpiredClientGateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
class PiiExpiredTestFlowConfiguration {

  @Bean
  @Autowired
  @ConditionalOnProperty(value = "test.flows.pii-expired.enabled", havingValue = "true")
  PersonalInformationDataExpiredClient personalInformationDataExpiredClient(
      PersonalInformationExpiredClientGateway personalInformationExpiredClientGateway) {

    log.info("PersonalInformationDataExpiredClient created");
    return new PersonalInformationDataExpiredClient(
        personalInformationExpiredClientGateway, new MessageGenerator());
  }
}
