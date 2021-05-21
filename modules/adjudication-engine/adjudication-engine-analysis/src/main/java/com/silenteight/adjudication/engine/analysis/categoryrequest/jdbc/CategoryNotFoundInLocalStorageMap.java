package com.silenteight.adjudication.engine.analysis.categoryrequest.jdbc;

public class CategoryNotFoundInLocalStorageMap
    extends RuntimeException {

  public CategoryNotFoundInLocalStorageMap(String category) {
    super(category);
  }
}
