package com.silenteight.warehouse.production.persistence.mapping.alert;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import javax.validation.constraints.NotNull;

import static java.util.List.of;

@Data
@Validated
@ConfigurationProperties(prefix = "warehouse.persistence.alert")
class AlertProperties {

  @NotNull
  private List<String> labels = of();
  @NotNull
  private String recommendationDateField;
}
