package com.silenteight.adjudication.engine.solve;

import org.springframework.messaging.handler.annotation.Payload;

public interface SolveGateway {

  @Payload("''")
  void startSolve();
}
