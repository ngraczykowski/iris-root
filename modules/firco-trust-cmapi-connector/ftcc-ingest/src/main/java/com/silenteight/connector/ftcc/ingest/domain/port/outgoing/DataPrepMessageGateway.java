package com.silenteight.connector.ftcc.ingest.domain.port.outgoing;

public interface DataPrepMessageGateway {

  void send(AlertMessage message);
}
