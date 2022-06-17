package com.silenteight.serp.governance.common.integration;

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

  @NonNull
  private List<String> exchanges = emptyList();
}
