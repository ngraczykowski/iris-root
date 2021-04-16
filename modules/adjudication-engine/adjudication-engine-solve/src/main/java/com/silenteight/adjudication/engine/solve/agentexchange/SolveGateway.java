package com.silenteight.adjudication.engine.solve.agentexchange;

import org.springframework.messaging.handler.annotation.Payload;

public interface SolveGateway {

  @Payload("''")
  void startSolve();
}
