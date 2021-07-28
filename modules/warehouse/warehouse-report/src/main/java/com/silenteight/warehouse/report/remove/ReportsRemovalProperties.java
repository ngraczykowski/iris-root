package com.silenteight.warehouse.report.remove;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;
import javax.validation.constraints.NotNull;

@Data
@Validated
@ConfigurationProperties(prefix = "warehouse.report.removal")
class ReportsRemovalProperties {

  @NotNull
  Duration durationTime;
}
