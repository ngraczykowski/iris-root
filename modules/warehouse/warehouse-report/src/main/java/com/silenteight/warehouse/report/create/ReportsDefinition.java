package com.silenteight.warehouse.report.create;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;

@Data
@ConstructorBinding
@Validated
@ConfigurationProperties(prefix = "warehouse.reports.v2")
public class ReportsDefinition {

  private List<ReportProperties> reports = new ArrayList<>();
}
