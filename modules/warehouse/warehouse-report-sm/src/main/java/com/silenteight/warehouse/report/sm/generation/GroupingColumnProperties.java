package com.silenteight.warehouse.report.sm.generation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import org.springframework.boot.context.properties.ConstructorBinding;

import java.util.List;

@ConstructorBinding
@AllArgsConstructor
@Getter
class GroupingColumnProperties {

  @NonNull
  String name;
  @NonNull
  List<String> decisionValues;
}
