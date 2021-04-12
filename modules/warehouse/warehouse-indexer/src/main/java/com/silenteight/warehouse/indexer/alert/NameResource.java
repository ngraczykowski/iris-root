package com.silenteight.warehouse.indexer.alert;

class NameResource {

  static String getId(String resourceName) {
    String[] splitedName = resourceName.split("/");
    int lastElement = splitedName.length - 1;
    return splitedName[lastElement];
  }
}
