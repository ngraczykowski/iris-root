package com.silenteight.serp.governance.model.archive.amqp;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Validated
@ConfigurationProperties("serp.governance.model.archived.integration.request")
@ConstructorBinding
@Data
class ModelsArchivedMessagingProperties {

  @NotBlank
  String exchange;

  @NotBlank
  String routingKey;
}
