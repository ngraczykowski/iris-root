package com.silenteight.warehouse.retention.production.personalinformation;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import javax.validation.Valid;

@Data
@Validated
@ConfigurationProperties(prefix = "warehouse.retention.personal-information")
@ConstructorBinding
class PersonalInformationProperties {

  @Valid
  private final int batchSize;

  @Valid
  private final List<String> fieldsToErase;
}
