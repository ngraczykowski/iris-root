package com.silenteight.warehouse.indexer.alert;

public class GroupingFieldNotFoundException extends RuntimeException {

  private static final long serialVersionUID = 1332523168821895928L;

  GroupingFieldNotFoundException(String fieldName) {
    super(String.format("Grouping field %s not found in database", fieldName));
  }
}
