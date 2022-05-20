package com.silenteight.adjudication.engine.analysis.categoryrequest.domain;

public class UnknownCategoryException extends RuntimeException {

  private static final long serialVersionUID = 7554121136067972337L;

  UnknownCategoryException(String category) {
    super(category);
  }
}
