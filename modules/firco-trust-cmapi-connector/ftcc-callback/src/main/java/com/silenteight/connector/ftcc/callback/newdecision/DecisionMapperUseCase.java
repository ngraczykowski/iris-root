package com.silenteight.connector.ftcc.callback.newdecision;

public interface DecisionMapperUseCase {

  DestinationStatus mapStatus(DecisionStatusRequest request);
}
