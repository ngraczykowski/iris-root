package com.silenteight.connector.ftcc.ingest.domain.port.outgoing;

import com.silenteight.proto.fab.api.v1.AlertMessageStored;

public interface DataPrepMessageGateway {

  void send(AlertMessageStored message);
}
