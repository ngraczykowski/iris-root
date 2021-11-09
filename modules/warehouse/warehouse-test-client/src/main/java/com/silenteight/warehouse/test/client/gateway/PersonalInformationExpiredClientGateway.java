package com.silenteight.warehouse.test.client.gateway;

import com.silenteight.dataretention.api.v1.PersonalInformationExpired;

public interface PersonalInformationExpiredClientGateway {

  void indexRequest(PersonalInformationExpired dataIndexRequest);
}
