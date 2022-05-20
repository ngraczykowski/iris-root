package com.silenteight.warehouse.simulation.processing.dbpartitioning;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
final class NameResource {

  static String getId(String resourceName) {
    String[] splitedName = resourceName.split("/");
    int lastElement = splitedName.length - 1;
    return splitedName[lastElement];
  }
}
