package com.silenteight.warehouse.sampling.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;

import com.silenteight.warehouse.indexer.alert.MultiValueEntry;
import com.silenteight.warehouse.indexer.query.common.QueryFilter;

import org.springframework.boot.context.properties.ConstructorBinding;

import java.util.List;
import javax.validation.constraints.NotNull;

@ConstructorBinding
@AllArgsConstructor
@Getter
public class FilterProperties {

  @NotNull
  private final String name;
  @NotNull
  private final List<String> values;

  static QueryFilter toQueryFilter(FilterProperties filterProperties) {
    return new QueryFilter(filterProperties.getName(), filterProperties.getValues());
  }

  public static MultiValueEntry toMultiValueEntry(FilterProperties filterProperties) {
    return new MultiValueEntry(filterProperties.getName(), filterProperties.getValues());
  }
}
