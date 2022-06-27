package com.silenteight.simulator.dataset.create;

import lombok.NonNull;
import lombok.Value;

import java.util.List;

@Value
public class DatasetLabel {

  @NonNull
  String name;
  @NonNull
  List<String> values;

  public boolean hasName(String name) {
    return this.name.equals(name);
  }
}
