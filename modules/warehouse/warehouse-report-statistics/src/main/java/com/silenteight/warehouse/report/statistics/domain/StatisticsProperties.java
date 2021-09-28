package com.silenteight.warehouse.report.statistics.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import com.silenteight.warehouse.indexer.query.common.QueryFilter;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import javax.annotation.Nullable;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import static java.util.Collections.emptyList;
import static java.util.List.of;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toList;

@AllArgsConstructor
@ConstructorBinding
@Getter
@Validated
@ConfigurationProperties(prefix = "warehouse.report.statistics")
class StatisticsProperties {

  @NotBlank
  private final String dateFieldName;
  @Valid
  @NonNull
  private final AiDecisionProperties aiDecision;
  @Valid
  @NonNull
  private final AnalystDecisionProperties analystDecision;

  @Valid
  @Nullable
  private final List<FilterProperties> filters;

  public List<String> getFields() {
    return of(aiDecision, analystDecision)
        .stream()
        .map(Decision::getField)
        .collect(toList());
  }

  List<QueryFilter> getQueryFilters() {
    if (isNull(getFilters()))
      return emptyList();

    return getFilters().stream()
        .map(FilterProperties::toQueryFilter)
        .collect(toList());
  }
}
