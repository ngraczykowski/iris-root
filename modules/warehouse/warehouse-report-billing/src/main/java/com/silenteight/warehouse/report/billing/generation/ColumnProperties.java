package com.silenteight.warehouse.report.billing.generation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import org.springframework.boot.context.properties.ConstructorBinding;

import java.util.List;

import static java.util.Collections.singletonList;

@AllArgsConstructor
@ConstructorBinding
@Getter
class ColumnProperties implements Column {

  @NonNull
  private final String name;
  @NonNull
  private final String label;

  public List<String> getLabels() {
    return singletonList(label);
  }
}
