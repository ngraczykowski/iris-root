package com.silenteight.serp.governance.qa.send.amqp;

import com.silenteight.data.api.v2.QaDataIndexRequest;

public interface AlertMessageGateway {

  void send(QaDataIndexRequest message);
}
