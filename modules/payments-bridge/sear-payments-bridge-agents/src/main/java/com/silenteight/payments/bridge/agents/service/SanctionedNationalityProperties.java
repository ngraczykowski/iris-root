package com.silenteight.payments.bridge.agents.service;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@ConfigurationProperties(prefix = "pb.agents.sanctioned-nationalities")
class SanctionedNationalityProperties {


  private static final String DEFAULT_SANCTIONED_NATIONALITIES = "NAT/IR,,NAT/SY,,NAT/CU";

  private String sanctionedNationalities = DEFAULT_SANCTIONED_NATIONALITIES;
}
