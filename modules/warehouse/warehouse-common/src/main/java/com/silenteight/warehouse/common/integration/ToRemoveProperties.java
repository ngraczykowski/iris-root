package com.silenteight.warehouse.common.integration;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static java.util.Collections.emptyList;

@Data
@RequiredArgsConstructor
class ToRemoveProperties {

  @NonNull
  private List<BindingProperties> bindings = emptyList();
}
