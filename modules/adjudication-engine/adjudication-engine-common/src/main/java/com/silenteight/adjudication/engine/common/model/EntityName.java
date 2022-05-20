package com.silenteight.adjudication.engine.common.model;

public class EntityName {

  public static final String getName(String entityName, Long id) {
    return entityName + "/" + id;
  }
}
