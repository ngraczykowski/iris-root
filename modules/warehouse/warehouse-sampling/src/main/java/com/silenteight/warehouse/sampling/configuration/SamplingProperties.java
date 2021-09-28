package com.silenteight.warehouse.sampling.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;

import com.silenteight.warehouse.indexer.query.common.QueryFilter;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import javax.annotation.Nullable;
import javax.validation.constraints.NotBlank;

import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

@AllArgsConstructor
@ConstructorBinding
@Getter
@Validated
@ConfigurationProperties(prefix = "warehouse.sampling")
public class SamplingProperties {

  @Nullable
  private final List<FilterProperties> filters;

  @NotBlank
  private final String timeFieldName;

  public List<FilterProperties> getFilters() {
    return ofNullable(filters)
        .orElse(emptyList());
  }

  public List<QueryFilter> getQueryFilters() {
    return getFilters().stream()
        .map(FilterProperties::toQueryFilter)
        .collect(toList());
  }
}
