package com.silenteight.sens.webapp.backend.decisiontree.exception;

import static java.lang.String.format;

public class DecisionTreeNotFoundException extends RuntimeException {

  private static final long serialVersionUID = 4635051932123201469L;

  private static final String MESSAGE = "Decision Tree with id=%s not found";

  public DecisionTreeNotFoundException(long id) {
    super(format(MESSAGE, id));
  }

  public DecisionTreeNotFoundException(long id, Throwable cause) {
    super(format(MESSAGE, id), cause);
  }
}
