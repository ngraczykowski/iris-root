package com.silenteight.searpayments.scb.mapper;

import lombok.RequiredArgsConstructor;

import com.silenteight.searpayments.bridge.dto.validator.AlertMessageDtoValidator;
import com.silenteight.searpayments.scb.etl.countrycode.CountryCodeExtractor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;

@RequiredArgsConstructor
@Configuration
class AlertConfiguration {

  private final CountryCodeExtractor countryCodeExtractor;
  private final CreateHitFactory createHitFactory;
  private final AlertMessageDtoValidator alertMessageDtoValidator;
  @Value("${tsaas.governance.dtp-message-type-null-alias:DTP}")
  private String dtpMessageTypeNullAlias;

  @Value("${tsaas.governance.dtp-message-type-null-alias:NBP}")
  private String nbpMessageTypeNullAlias;

  @Value("${git.commit.id}")
  private String gitCommitId;

  @Bean
  public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
    PropertySourcesPlaceholderConfigurer propsConfig
        = new PropertySourcesPlaceholderConfigurer();
    propsConfig.setLocation(new ClassPathResource("git.properties"));
    propsConfig.setIgnoreResourceNotFound(true);
    propsConfig.setIgnoreUnresolvablePlaceholders(true);
    return propsConfig;
  }

  @Bean
  CreateAlertFactory createAlertFromMessageFactory() {
    return new CreateAlertFactory(
        createHitsFactory(), createMessageTypeFactory(), countryCodeExtractor,
        createBasicAlertFactory(), gitCommitId.substring(0, 7), alertMessageDtoValidator);
  }

  @Bean
  CreateMessageTypeFactory createMessageTypeFactory() {
    return new CreateMessageTypeFactory(dtpMessageTypeNullAlias, nbpMessageTypeNullAlias);
  }

  @Bean
  CreateBasicAlertFactory createBasicAlertFactory() {
    return new CreateBasicAlertFactory(gitCommitId.substring(0, 7));
  }

  @Bean
  CreateHitsFactory createHitsFactory() {
    return new CreateHitsFactory(createHitFactory);
  }
}
