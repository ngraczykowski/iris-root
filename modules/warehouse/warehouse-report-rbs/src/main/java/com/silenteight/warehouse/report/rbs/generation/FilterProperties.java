package com.silenteight.warehouse.report.rbs.generation;

import lombok.AllArgsConstructor;
import lombok.Getter;

import com.silenteight.warehouse.indexer.query.grouping.QueryFilter;

import org.springframework.boot.context.properties.ConstructorBinding;

import java.util.List;
import javax.validation.constraints.NotNull;

@ConstructorBinding
@AllArgsConstructor
@Getter
class FilterProperties {

  @NotNull
  private final String name;
  @NotNull
  private final List<String> values;

  static QueryFilter toQueryFilter(FilterProperties filterProperties) {
    return new QueryFilter(filterProperties.getName(), filterProperties.getValues());
  }
}
