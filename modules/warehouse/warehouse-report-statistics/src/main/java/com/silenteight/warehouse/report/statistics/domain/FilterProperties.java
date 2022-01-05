package com.silenteight.warehouse.report.statistics.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import com.silenteight.warehouse.indexer.query.common.QueryFilter;

import org.springframework.boot.context.properties.ConstructorBinding;

import java.util.List;
import javax.validation.constraints.NotNull;

@ConstructorBinding
@AllArgsConstructor
@Getter
@Deprecated(since = "2.0.0")
class FilterProperties {

  @NotNull
  private final String name;
  @NotNull
  private final List<String> values;

  static QueryFilter toQueryFilter(FilterProperties filterProperties) {
    return new QueryFilter(filterProperties.getName(), filterProperties.getValues());
  }
}
