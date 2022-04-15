package com.silenteight.simulator.management.domain;

public class InvalidModelNameException extends RuntimeException {

  private static final long serialVersionUID = -4750750865053365222L;

  InvalidModelNameException(String modelName) {
    super(String.format("There is no simulation with assigned modelName=%s.", modelName));
  }
}
