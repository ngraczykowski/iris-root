package com.silenteight.warehouse.report.reasoning.match.generation;

import lombok.AllArgsConstructor;
import lombok.Getter;

import com.silenteight.warehouse.indexer.query.common.QueryFilter;

import org.springframework.boot.context.properties.ConstructorBinding;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@ConstructorBinding
@AllArgsConstructor
@Getter
class FilterProperties {

  @NotNull
  private final String name;
  @NotNull
  private final List<String> allowedValues;

  static QueryFilter toQueryFilter(@Valid FilterProperties filterProperties) {
    return new QueryFilter(filterProperties.getName(), filterProperties.getAllowedValues());
  }
}
