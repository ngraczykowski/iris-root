package com.silenteight.serp.governance.qa.retention.personalinformation;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Data
@Validated
@ConfigurationProperties(prefix = "serp.governance.qa.retention.personal-information")
@ConstructorBinding
class PersonalInformationProperties {

  @Valid
  private int batchSize;
}
