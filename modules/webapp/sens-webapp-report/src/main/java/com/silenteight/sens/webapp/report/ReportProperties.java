package com.silenteight.sens.webapp.report;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import com.silenteight.sens.webapp.report.list.FilterType;

import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Validated
@Data
@RequiredArgsConstructor
@ConstructorBinding
public abstract class ReportProperties {

  @NotBlank
  protected String name;
  @NotBlank
  protected String type;
  @NotBlank
  protected String label;
  @NotNull
  protected boolean enabled;
  @NotNull
  @Valid
  protected FilterType filterType;
}
