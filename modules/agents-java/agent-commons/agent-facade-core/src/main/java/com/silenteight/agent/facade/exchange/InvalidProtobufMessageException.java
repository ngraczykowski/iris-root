package com.silenteight.agent.facade.exchange;

import com.google.protobuf.Any;

import static java.lang.String.format;

public class InvalidProtobufMessageException extends RuntimeException {

  private static final long serialVersionUID = 5771982922432420376L;

  public InvalidProtobufMessageException(Any any, Throwable cause) {
    super(format("Cannot transform any message: %s", any), cause);
  }
}
