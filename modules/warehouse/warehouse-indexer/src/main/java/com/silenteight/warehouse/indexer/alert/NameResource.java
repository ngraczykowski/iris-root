package com.silenteight.warehouse.indexer.alert;

class NameResource {

  static String getSplitName(String resourceName) {
    String[] splitName = resourceName.split("/");
    int lastElement = splitName.length - 1;
    return splitName[lastElement];
  }
}
