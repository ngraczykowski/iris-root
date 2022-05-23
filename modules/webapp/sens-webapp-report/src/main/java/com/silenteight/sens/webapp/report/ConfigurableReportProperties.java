package com.silenteight.sens.webapp.report;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import javax.validation.Valid;

@Validated
@RequiredArgsConstructor
@ConstructorBinding
@Data
@ConfigurationProperties(prefix = "sens.webapp")
class ConfigurableReportProperties {

  @Valid
  @NonNull
  private List<Report> reports;

  @Data
  private static class Report {
    @Valid
    @NonNull
    private String name;
    @Valid
    @NonNull
    private String label;
  }
}